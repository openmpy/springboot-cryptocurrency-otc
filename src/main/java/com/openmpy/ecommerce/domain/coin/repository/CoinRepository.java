package com.openmpy.ecommerce.domain.coin.repository;

import com.openmpy.ecommerce.domain.coin.entity.CoinEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepository extends JpaRepository<CoinEntity, Long> {

    boolean existsByMarket(String market);
}
