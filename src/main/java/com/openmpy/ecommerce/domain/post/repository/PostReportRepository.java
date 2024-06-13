package com.openmpy.ecommerce.domain.post.repository;

import com.openmpy.ecommerce.domain.member.entity.MemberEntity;
import com.openmpy.ecommerce.domain.post.entity.PostEntity;
import com.openmpy.ecommerce.domain.post.entity.PostReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostReportRepository extends JpaRepository<PostReportEntity, Long> {

    boolean existsByMemberEntityAndPostEntity(MemberEntity memberEntity, PostEntity postEntity);

    List<PostReportEntity> findAllByPostEntity(PostEntity postEntity);
}
