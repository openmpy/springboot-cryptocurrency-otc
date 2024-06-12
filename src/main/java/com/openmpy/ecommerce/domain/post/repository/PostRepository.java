package com.openmpy.ecommerce.domain.post.repository;

import com.openmpy.ecommerce.domain.post.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    @Query("select p from PostEntity p where p.title LIKE %:query% OR p.content LIKE %:query%")
    Page<PostEntity> findByQuery(@Param("query") String query, Pageable pageable);
}
