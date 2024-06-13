package com.openmpy.ecommerce.domain.wallet.entity;

import com.openmpy.ecommerce.domain.coin.entity.CoinEntity;
import com.openmpy.ecommerce.domain.member.entity.MemberEntity;
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
public class WalletEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coin_id")
    private CoinEntity coinEntity;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private BigDecimal average;

    @Builder
    public WalletEntity(MemberEntity memberEntity, CoinEntity coinEntity, BigDecimal amount, BigDecimal average) {
        this.memberEntity = memberEntity;
        this.coinEntity = coinEntity;
        this.amount = amount;
        this.average = average;
    }

    public void minusAmount(BigDecimal amount) {
        this.amount = this.amount.subtract(amount);
    }
}
