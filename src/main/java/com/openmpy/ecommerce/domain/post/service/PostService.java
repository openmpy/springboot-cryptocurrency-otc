package com.openmpy.ecommerce.domain.post.service;

import com.openmpy.ecommerce.domain.member.entity.MemberEntity;
import com.openmpy.ecommerce.domain.member.repository.MemberRepository;
import com.openmpy.ecommerce.domain.post.dto.request.CreatePostRequestDto;
import com.openmpy.ecommerce.domain.post.dto.request.UpdatePostRequestDto;
import com.openmpy.ecommerce.domain.post.dto.response.CreatePostResponseDto;
import com.openmpy.ecommerce.domain.post.dto.response.GetPostResponseDto;
import com.openmpy.ecommerce.domain.post.dto.response.UpdatePostResponseDto;
import com.openmpy.ecommerce.domain.post.entity.PostEntity;
import com.openmpy.ecommerce.domain.post.entity.PostImageEntity;
import com.openmpy.ecommerce.domain.post.repository.PostImageRepository;
import com.openmpy.ecommerce.domain.post.repository.PostRepository;
import com.openmpy.ecommerce.global.exception.CustomException;
import com.openmpy.ecommerce.global.exception.constants.ErrorCode;
import com.openmpy.ecommerce.global.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {

    private static final int MAX_UPLOAD_IMAGE_COUNT = 3;
    private static final int MAX_UPLOAD_IMAGE_SIZE = 5 * 1024 * 1024;
    private static final List<String> UPLOAD_IMAGE_TYPE = List.of(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );
    private static final Logger log = LoggerFactory.getLogger(PostService.class);

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final S3Service s3Service;
    private final PostImageRepository postImageRepository;

    @Transactional
    public CreatePostResponseDto create(String email, CreatePostRequestDto requestDto, List<MultipartFile> multipartFiles) {
        MemberEntity memberEntity = validateMemberEntity(email);

        PostEntity postEntity = postRepository.save(requestDto.create(memberEntity));

        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            handlePostImages(multipartFiles, postEntity);
        }

        updateThumbnail(postEntity);
        return new CreatePostResponseDto(postEntity);
    }

    public GetPostResponseDto get(Long postId) {
        PostEntity postEntity = validatePostEntity(postId);
        List<PostImageEntity> postImageEntities = postImageRepository.findAllByPostEntity(postEntity);

        return new GetPostResponseDto(postEntity, postImageEntities);
    }

    public Page<GetPostResponseDto> gets(int page, int size) {
        PageRequest pageRequest = PageRequest.of(Math.max(0, page), size, Sort.Direction.DESC, "createdAt");
        Page<PostEntity> pagedPosts = postRepository.findAll(pageRequest);
        List<GetPostResponseDto> postResponses = getGetPostResponseDtos(pagedPosts);
        return new PageImpl<>(postResponses, pageRequest, pagedPosts.getTotalElements());
    }

    @Transactional
    public UpdatePostResponseDto update(String email, Long postId, UpdatePostRequestDto requestDto) {
        MemberEntity memberEntity = validateMemberEntity(email);
        PostEntity postEntity = validatePostEntity(postId);

        if (!postEntity.getWriter().equals(memberEntity)) {
            throw new CustomException(ErrorCode.INVALID_POST_MEMBER);
        }

        postEntity.update(requestDto.title(), requestDto.content());
        return new UpdatePostResponseDto(postEntity);
    }

    @Transactional
    public void delete(String email, Long postId) {
        MemberEntity memberEntity = validateMemberEntity(email);
        PostEntity postEntity = validatePostEntity(postId);

        if (!postEntity.getWriter().equals(memberEntity)) {
            throw new CustomException(ErrorCode.INVALID_POST_MEMBER);
        }

        postImageRepository.findAllByPostEntity(postEntity).forEach(postImageEntity -> {
            s3Service.deleteFile(postImageEntity.getImageUrl());
            postImageRepository.delete(postImageEntity);
        });

        postRepository.delete(postEntity);
    }

    public Page<GetPostResponseDto> search(String query, int page, int size) {
        PageRequest pageRequest = PageRequest.of(Math.max(0, page), size, Sort.Direction.DESC, "createdAt");
        Page<PostEntity> pagedPosts = postRepository.findByQuery(query, pageRequest);
        List<GetPostResponseDto> postResponses = getGetPostResponseDtos(pagedPosts);
        return new PageImpl<>(postResponses, pageRequest, pagedPosts.getTotalElements());
    }

    private PostEntity validatePostEntity(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));
    }

    private MemberEntity validateMemberEntity(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    private List<GetPostResponseDto> getGetPostResponseDtos(Page<PostEntity> pagedPosts) {
        List<GetPostResponseDto> postResponses = new ArrayList<>();

        pagedPosts.forEach(postEntity -> {
            List<PostImageEntity> postImageEntities = postImageRepository.findAllByPostEntity(postEntity);
            postResponses.add(new GetPostResponseDto(postEntity, postImageEntities));
        });
        return postResponses;
    }

    private void handlePostImages(List<MultipartFile> multipartFiles, PostEntity postEntity) {
        if (multipartFiles.size() > MAX_UPLOAD_IMAGE_COUNT) {
            throw new CustomException(ErrorCode.TOO_MANY_POST_IMAGE);
        }

        for (MultipartFile file : multipartFiles) {
            if (file.getSize() > MAX_UPLOAD_IMAGE_SIZE) {
                throw new CustomException(ErrorCode.TOO_LONG_POST_IMAGE_SIZE);
            }
            if (!UPLOAD_IMAGE_TYPE.contains(file.getContentType())) {
                throw new CustomException(ErrorCode.INVALID_POST_IMAGE_TYPE);
            }

            try {
                String originalFilename = file.getOriginalFilename();
                String extension = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf("."));
                String randomImageUrl = UUID.randomUUID() + extension;

                PostImageEntity postImageEntity = PostImageEntity.builder()
                        .imageUrl(randomImageUrl)
                        .postEntity(postEntity)
                        .build();

                postImageRepository.save(postImageEntity);
                s3Service.uploadFile(randomImageUrl, file);
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new CustomException(ErrorCode.UNKNOWN_POST_IMAGE_UPLOAD);
            }
        }
    }

    private void updateThumbnail(PostEntity postEntity) {
        postImageRepository.findFirstByPostEntity(postEntity)
                .ifPresent(imageEntity -> postEntity.updateThumbnail(imageEntity.getImageUrl()));
    }
}
