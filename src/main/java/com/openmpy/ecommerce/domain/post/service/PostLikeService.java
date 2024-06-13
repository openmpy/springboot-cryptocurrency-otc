package com.openmpy.ecommerce.domain.post.service;

import com.openmpy.ecommerce.domain.member.entity.MemberEntity;
import com.openmpy.ecommerce.domain.member.repository.MemberRepository;
import com.openmpy.ecommerce.domain.post.entity.PostEntity;
import com.openmpy.ecommerce.domain.post.entity.PostLikeEntity;
import com.openmpy.ecommerce.domain.post.repository.PostLikeRepository;
import com.openmpy.ecommerce.domain.post.repository.PostRepository;
import com.openmpy.ecommerce.global.exception.CustomException;
import com.openmpy.ecommerce.global.exception.constants.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class PostLikeService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final MemberRepository memberRepository;

    public void like(String email, Long postId) {
        MemberEntity memberEntity = validateMemberEntity(email);
        PostEntity postEntity = validatePostEntity(postId);

        if (postLikeRepository.existsByMemberEntityAndPostEntity(memberEntity, postEntity)) {
            throw new CustomException(ErrorCode.ALREADY_LIKE_POST);
        }

        PostLikeEntity postLikeEntity = PostLikeEntity.builder()
                .memberEntity(memberEntity)
                .postEntity(postEntity)
                .build();

        postLikeRepository.save(postLikeEntity);
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
