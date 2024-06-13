package com.openmpy.ecommerce.domain.member.dto.request;

import com.openmpy.ecommerce.domain.member.entity.MemberEntity;
import com.openmpy.ecommerce.domain.member.entity.constants.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record SignupMemberRequestDto(
        @Email(message = "이메일 형식으로 입력해주시길 바랍니다.")
        String email,

        @NotBlank(message = "비밀번호를 입력해주시길 바랍니다.")
        String password
) {

    public MemberEntity signup(String encodedPassword) {
        return MemberEntity.builder()
                .email(this.email)
                .password(encodedPassword)
                .role(RoleType.ROLE_USER)
                .balance(BigDecimal.ZERO)
                .build();
    }
}
