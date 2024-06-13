package com.openmpy.ecommerce.domain.member.dto.response;

import com.openmpy.ecommerce.domain.member.entity.MemberEntity;
import com.openmpy.ecommerce.domain.member.entity.constants.RoleType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GetMemberResponseDto(
        String email,
        RoleType role,
        BigDecimal balance,
        LocalDateTime createdAt
) {
    public GetMemberResponseDto(MemberEntity memberEntity) {
        this(memberEntity.getEmail(), memberEntity.getRole(), memberEntity.getBalance(), memberEntity.getCreatedAt());
    }
}
