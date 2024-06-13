package com.openmpy.ecommerce.domain.post.repository;

import com.openmpy.ecommerce.domain.member.entity.MemberEntity;
import com.openmpy.ecommerce.domain.post.entity.PostEntity;
import com.openmpy.ecommerce.domain.post.entity.PostLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLikeEntity, Long> {

    boolean existsByMemberEntityAndPostEntity(MemberEntity memberEntity, PostEntity postEntity);

    long countByPostEntity(PostEntity postEntity);

    List<PostLikeEntity> findAllByPostEntity(PostEntity postEntity);
}
