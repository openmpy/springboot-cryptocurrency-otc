package com.openmpy.ecommerce.domain.coin.entity;

import com.openmpy.ecommerce.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class CoinEntity extends BaseEntity {

    @Column(nullable = false)
    private String market;

    @Column(nullable = false)
    private String koreanName;

    @Column(nullable = false)
    private String englishName;

    @Builder
    public CoinEntity(String market, String koreanName, String englishName) {
        this.market = market;
        this.koreanName = koreanName;
        this.englishName = englishName;
    }
}
