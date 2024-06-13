package com.openmpy.ecommerce.domain.coin.controller;

import com.openmpy.ecommerce.domain.coin.dto.response.GetUpbitCoinResponseDto;
import com.openmpy.ecommerce.domain.coin.service.CoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/coins")
@RestController
public class CoinController {

    private final CoinService coinService;

    @GetMapping("/{coinId}")
    public ResponseEntity<GetUpbitCoinResponseDto> get(@PathVariable Long coinId) {
        GetUpbitCoinResponseDto responseDto = coinService.get(coinId);
        return ResponseEntity.ok(responseDto);
    }
}
