package com.openmpy.ecommerce.domain.post.dto.response;

import com.openmpy.ecommerce.domain.post.entity.PostEntity;

public record UpdatePostResponseDto(
        String title,
        String content
) {
    public UpdatePostResponseDto(PostEntity postEntity) {
        this(postEntity.getTitle(), postEntity.getContent());
    }
}
