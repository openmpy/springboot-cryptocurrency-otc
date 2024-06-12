package com.openmpy.ecommerce.domain.member.dto.response;

import com.openmpy.ecommerce.domain.member.entity.MemberEntity;

import java.time.LocalDateTime;

public record SignupMemberResponseDto(
        String email,
        LocalDateTime createdAt
) {

    public SignupMemberResponseDto(MemberEntity memberEntity) {
        this(memberEntity.getEmail(), memberEntity.getCreatedAt());
    }
}
