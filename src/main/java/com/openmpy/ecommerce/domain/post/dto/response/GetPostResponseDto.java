package com.openmpy.ecommerce.domain.post.dto.response;

import com.openmpy.ecommerce.domain.post.entity.PostEntity;
import com.openmpy.ecommerce.domain.post.entity.PostImageEntity;

import java.time.LocalDateTime;
import java.util.List;

public record GetPostResponseDto(
        Long id,
        String title,
        String content,
        String writer,
        LocalDateTime createdAt,
        List<String> imageUrls
) {
    public GetPostResponseDto(PostEntity postEntity, List<PostImageEntity> postImageEntities) {
        this(
                postEntity.getId(),
                postEntity.getTitle(),
                postEntity.getContent(),
                postEntity.getWriter().getEmail(),
                postEntity.getCreatedAt(),
                postImageEntities.stream()
                        .map(PostImageEntity::getImageUrl)
                        .toList()
        );
    }
}
