package com.openmpy.ecommerce.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdatePostRequestDto(
        @NotBlank(message = "제목을 입력해주시길 바랍니다.")
        String title,

        @NotBlank(message = "내용을 입력해주시길 바랍니다.")
        String content
) {
}
