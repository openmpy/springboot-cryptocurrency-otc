package com.openmpy.ecommerce.domain.member.dto.response;

import com.openmpy.ecommerce.domain.member.entity.MemberEntity;
import com.openmpy.ecommerce.domain.member.entity.constants.RoleType;

import java.time.LocalDateTime;

public record GetMemberResponseDto(
        String email,
        RoleType role,
        LocalDateTime createdAt
) {
    public GetMemberResponseDto(MemberEntity memberEntity) {
        this(memberEntity.getEmail(), memberEntity.getRole(), memberEntity.getCreatedAt());
    }
}
