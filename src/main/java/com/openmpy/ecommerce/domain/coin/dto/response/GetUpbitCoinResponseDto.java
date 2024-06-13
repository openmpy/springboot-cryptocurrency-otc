package com.openmpy.ecommerce.domain.coin.dto.response;

import com.openmpy.ecommerce.domain.coin.entity.CoinEntity;

public record GetUpbitCoinResponseDto(
        Long id,
        String market,
        String koreanName,
        String englishName
) {
    public GetUpbitCoinResponseDto(CoinEntity coinEntity) {
        this(coinEntity.getId(), coinEntity.getMarket(), coinEntity.getKoreanName(), coinEntity.getEnglishName());
    }
}
