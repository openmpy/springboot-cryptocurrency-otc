package com.openmpy.ecommerce.domain.post.repository;

import com.openmpy.ecommerce.domain.post.entity.PostEntity;
import com.openmpy.ecommerce.domain.post.entity.PostImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostImageRepository extends JpaRepository<PostImageEntity, Long> {

    Optional<PostImageEntity> findFirstByPostEntity(PostEntity postEntity);

    List<PostImageEntity> findAllByPostEntity(PostEntity postEntity);
}
