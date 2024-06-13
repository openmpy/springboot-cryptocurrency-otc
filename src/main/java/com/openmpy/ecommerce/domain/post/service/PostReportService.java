package com.openmpy.ecommerce.domain.post.service;

import com.openmpy.ecommerce.domain.member.entity.MemberEntity;
import com.openmpy.ecommerce.domain.member.repository.MemberRepository;
import com.openmpy.ecommerce.domain.post.entity.PostEntity;
import com.openmpy.ecommerce.domain.post.entity.PostReportEntity;
import com.openmpy.ecommerce.domain.post.repository.PostReportRepository;
import com.openmpy.ecommerce.domain.post.repository.PostRepository;
import com.openmpy.ecommerce.global.exception.CustomException;
import com.openmpy.ecommerce.global.exception.constants.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class PostReportService {

    private static final int MAX_REPORT_COUNT = 5;

    private final PostRepository postRepository;
    private final PostReportRepository postReportRepository;
    private final MemberRepository memberRepository;

    public void report(String email, Long postId) {
        MemberEntity memberEntity = validateMemberEntity(email);
        PostEntity postEntity = validatePostEntity(postId);

        if (postReportRepository.existsByMemberEntityAndPostEntity(memberEntity, postEntity)) {
            throw new CustomException(ErrorCode.ALREADY_REPORT_POST);
        }

        PostReportEntity postReportEntity = PostReportEntity.builder()
                .memberEntity(memberEntity)
                .postEntity(postEntity)
                .build();

        postReportRepository.save(postReportEntity);
        checkReportPost(postEntity);
    }

    private PostEntity validatePostEntity(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));
    }

    private MemberEntity validateMemberEntity(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    private void checkReportPost(PostEntity postEntity) {
        int reportCount = postReportRepository.countByPostEntity(postEntity);
        if (reportCount >= MAX_REPORT_COUNT) {
            postEntity.updateDelete(Boolean.TRUE);
        }
    }
}
