package com.openmpy.ecommerce.domain.post.service;

import com.openmpy.ecommerce.domain.member.entity.MemberEntity;
import com.openmpy.ecommerce.domain.member.repository.MemberRepository;
import com.openmpy.ecommerce.domain.post.dto.request.CreatePostRequestDto;
import com.openmpy.ecommerce.domain.post.dto.request.UpdatePostRequestDto;
import com.openmpy.ecommerce.domain.post.dto.response.CreatePostResponseDto;
import com.openmpy.ecommerce.domain.post.dto.response.GetPostResponseDto;
import com.openmpy.ecommerce.domain.post.dto.response.UpdatePostResponseDto;
import com.openmpy.ecommerce.domain.post.entity.PostEntity;
import com.openmpy.ecommerce.domain.post.repository.PostRepository;
import com.openmpy.ecommerce.global.exception.CustomException;
import com.openmpy.ecommerce.global.exception.constants.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public CreatePostResponseDto create(String email, CreatePostRequestDto requestDto) {
        MemberEntity memberEntity = validateMemberEntity(email);

        PostEntity postEntity = postRepository.save(requestDto.create(memberEntity));
        return new CreatePostResponseDto(postEntity);
    }

    public GetPostResponseDto get(Long postId) {
        PostEntity postEntity = validatePostEntity(postId);

        return new GetPostResponseDto(postEntity);
    }

    public Page<GetPostResponseDto> gets(int page, int size) {
        PageRequest pageRequest = PageRequest.of(Math.max(0, page), size, Sort.Direction.DESC, "createdAt");
        Page<PostEntity> pagedPosts = postRepository.findAll(pageRequest);
        List<GetPostResponseDto> postResponses = pagedPosts.stream()
                .map(GetPostResponseDto::new)
                .toList();
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

        postRepository.delete(postEntity);
    }

    public Page<GetPostResponseDto> search(String query, int page, int size) {
        PageRequest pageRequest = PageRequest.of(Math.max(0, page), size, Sort.Direction.DESC, "createdAt");
        Page<PostEntity> pagedPosts = postRepository.findByQuery(query, pageRequest);
        List<GetPostResponseDto> postResponses = pagedPosts.stream()
                .map(GetPostResponseDto::new)
                .toList();
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
}
