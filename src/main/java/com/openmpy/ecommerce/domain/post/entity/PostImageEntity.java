package com.openmpy.ecommerce.domain.post.entity;

import com.openmpy.ecommerce.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class PostImageEntity extends BaseEntity {

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity postEntity;

    @Builder
    public PostImageEntity(String imageUrl, PostEntity postEntity) {
        this.imageUrl = imageUrl;
        this.postEntity = postEntity;
    }
}
