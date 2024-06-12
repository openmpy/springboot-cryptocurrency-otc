package com.openmpy.ecommerce.domain.post.dto.response;

import com.openmpy.ecommerce.domain.post.entity.PostEntity;

import java.time.LocalDateTime;

public record GetPostResponseDto(
        Long id,
        String title,
        String content,
        String writer,
        LocalDateTime createdAt
) {
    public GetPostResponseDto(PostEntity postEntity) {
        this(
                postEntity.getId(),
                postEntity.getTitle(),
                postEntity.getContent(),
                postEntity.getWriter().getEmail(),
                postEntity.getCreatedAt()
        );
    }
}