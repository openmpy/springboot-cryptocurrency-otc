package com.openmpy.ecommerce.domain.post.repository;

import com.openmpy.ecommerce.domain.post.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
}
