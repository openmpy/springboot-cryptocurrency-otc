package com.openmpy.ecommerce.domain.trade.dto.request;

import com.openmpy.ecommerce.domain.coin.entity.CoinEntity;
import com.openmpy.ecommerce.domain.member.entity.MemberEntity;
import com.openmpy.ecommerce.domain.trade.entity.TradeEntity;
import com.openmpy.ecommerce.domain.trade.entity.constants.TradeType;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record BuyTradeRequestDto(
        Long coinId,

        @Positive(message = "수량은 양수만 가능합니다.")
        BigDecimal amount,

        @Positive(message = "가격은 양수만 가능합니다.")
        BigDecimal price
) {
    public TradeEntity buy(MemberEntity memberEntity, CoinEntity coinEntity) {
        return TradeEntity.builder()
                .tradeType(TradeType.BUY)
                .amount(this.amount)
                .price(this.price)
                .memberEntity(memberEntity)
                .coinEntity(coinEntity)
                .build();
    }
}
