package com.openmpy.ecommerce.domain.coin.dto.response;

import java.util.List;

public record ListUpbitCoinResponseDto(
        List<GetUpbitCoinResponseDto> coins
) {
}
