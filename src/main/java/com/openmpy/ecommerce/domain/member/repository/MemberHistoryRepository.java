package com.openmpy.ecommerce.domain.member.repository;

import com.openmpy.ecommerce.domain.member.entity.MemberHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberHistoryRepository extends JpaRepository<MemberHistoryEntity, Long> {
}
