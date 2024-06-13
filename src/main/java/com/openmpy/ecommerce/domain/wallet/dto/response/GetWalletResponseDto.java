package com.openmpy.ecommerce.domain.wallet.dto.response;

import com.openmpy.ecommerce.domain.wallet.entity.WalletEntity;

import java.math.BigDecimal;

public record GetWalletResponseDto(
        String koreanName,
        String englishName,
        BigDecimal amount,
        BigDecimal average
) {
    public GetWalletResponseDto(WalletEntity walletEntity) {
        this(
                walletEntity.getCoinEntity().getKoreanName(),
                walletEntity.getCoinEntity().getEnglishName(),
                walletEntity.getAmount(),
                walletEntity.getAverage()
        );
    }
}
