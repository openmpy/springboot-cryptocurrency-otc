package com.openmpy.ecommerce.domain.trade.repository;

import com.openmpy.ecommerce.domain.trade.entity.TradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<TradeEntity, Long> {
}
