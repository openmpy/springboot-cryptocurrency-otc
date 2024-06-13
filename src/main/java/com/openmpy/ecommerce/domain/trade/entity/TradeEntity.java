package com.openmpy.ecommerce.domain.trade.entity;

import com.openmpy.ecommerce.domain.coin.entity.CoinEntity;
import com.openmpy.ecommerce.domain.member.entity.MemberEntity;
import com.openmpy.ecommerce.domain.trade.entity.constants.TradeType;
import com.openmpy.ecommerce.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class TradeEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TradeType tradeType;

    @Column(nullable = false)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal price = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coin_id")
    private CoinEntity coinEntity;

    @Builder
    public TradeEntity(TradeType tradeType, BigDecimal amount, BigDecimal price, MemberEntity memberEntity, CoinEntity coinEntity) {
        this.tradeType = tradeType;
        this.amount = amount;
        this.price = price;
        this.memberEntity = memberEntity;
        this.coinEntity = coinEntity;
    }
}
