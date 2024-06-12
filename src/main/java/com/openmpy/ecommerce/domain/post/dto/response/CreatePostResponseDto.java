package com.openmpy.ecommerce.domain.post.dto.response;

import com.openmpy.ecommerce.domain.post.entity.PostEntity;

import java.time.LocalDateTime;

public record CreatePostResponseDto(
        String title,
        String content,
        LocalDateTime createdAt
) {
    public CreatePostResponseDto(PostEntity postEntity) {
        this(postEntity.getTitle(), postEntity.getContent(), postEntity.getCreatedAt());
    }
}
