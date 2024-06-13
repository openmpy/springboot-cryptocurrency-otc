package com.openmpy.ecommerce.domain.coin.dto.request;

import com.openmpy.ecommerce.domain.coin.entity.CoinEntity;

public record GetUpbitCoinRequestDto(
        String market,
        String korean_name,
        String english_name
) {
    public CoinEntity fetch() {
        return CoinEntity.builder()
                .market(this.market)
                .koreanName(this.korean_name)
                .englishName(this.english_name)
                .build();
    }
}
