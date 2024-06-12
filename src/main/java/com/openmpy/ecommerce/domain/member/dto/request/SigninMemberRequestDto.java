package com.openmpy.ecommerce.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SigninMemberRequestDto(
        @Email(message = "이메일 형식으로 입력해주시길 바랍니다.")
        String email,

        @NotBlank(message = "비밀번호를 입력해주시길 바랍니다.")
        String password
) {
}
