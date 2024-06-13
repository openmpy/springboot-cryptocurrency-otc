package com.openmpy.ecommerce.domain.wallet.dto.response;

import java.util.List;

public record ListWalletResponseDto(
        List<GetWalletResponseDto> wallets
) {
}
