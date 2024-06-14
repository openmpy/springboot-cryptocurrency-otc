package com.openmpy.ecommerce.domain.trade.repository;

import com.openmpy.ecommerce.domain.trade.entity.TradeEntity;
import com.openmpy.ecommerce.domain.trade.entity.constants.TradeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<TradeEntity, Long> {

    Page<TradeEntity> findAllByTradeType(TradeType tradeType, Pageable pageable);
}
