package com.openmpy.ecommerce.domain.trade.dto.response;

import com.openmpy.ecommerce.domain.trade.entity.TradeEntity;
import com.openmpy.ecommerce.domain.trade.entity.constants.TradeType;

import java.math.BigDecimal;

public record GetTradeResponseDto(
        Long id,
        TradeType tradeType,
        BigDecimal amount,
        BigDecimal price,
        Long memberId,
        Long coinId
) {

    public GetTradeResponseDto(TradeEntity tradeEntity) {
        this(
                tradeEntity.getId(),
                tradeEntity.getTradeType(),
                tradeEntity.getAmount(),
                tradeEntity.getPrice(),
                tradeEntity.getMemberEntity().getId(),
                tradeEntity.getCoinEntity().getId()
        );
    }
}
