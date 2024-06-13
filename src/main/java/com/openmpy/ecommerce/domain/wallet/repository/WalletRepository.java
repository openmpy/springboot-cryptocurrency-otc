package com.openmpy.ecommerce.domain.wallet.repository;

import com.openmpy.ecommerce.domain.member.entity.MemberEntity;
import com.openmpy.ecommerce.domain.wallet.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletRepository extends JpaRepository<WalletEntity, Long> {

    List<WalletEntity> findAllByMemberEntity(MemberEntity memberEntity);
}
