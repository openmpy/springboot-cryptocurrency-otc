package com.openmpy.ecommerce.domain.post.dto.request;

import com.openmpy.ecommerce.domain.member.entity.MemberEntity;
import com.openmpy.ecommerce.domain.post.entity.PostEntity;
import jakarta.validation.constraints.NotBlank;

public record CreatePostRequestDto(
        @NotBlank(message = "제목을 입력해주시길 바랍니다.")
        String title,

        @NotBlank(message = "내용을 입력해주시길 바랍니다.")
        String content
) {
    public PostEntity create(MemberEntity memberEntity) {
        return PostEntity.builder()
                .title(title)
                .content(content)
                .writer(memberEntity)
                .build();
    }
}
