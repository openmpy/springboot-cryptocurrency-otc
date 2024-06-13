package com.openmpy.ecommerce.domain.trade.dto.response;

import com.openmpy.ecommerce.domain.trade.entity.TradeEntity;

import java.math.BigDecimal;

public record BuyTradeResponseDto(
        Long coinId,
        BigDecimal amount,
        BigDecimal price
) {
    public BuyTradeResponseDto(TradeEntity tradeEntity) {
        this(tradeEntity.getCoinEntity().getId(), tradeEntity.getAmount(), tradeEntity.getPrice());
    }
}
