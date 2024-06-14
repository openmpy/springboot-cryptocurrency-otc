package com.openmpy.ecommerce.domain.coin.controller;

import com.openmpy.ecommerce.domain.coin.dto.response.GetUpbitCoinResponseDto;
import com.openmpy.ecommerce.domain.coin.dto.response.ListUpbitCoinResponseDto;
import com.openmpy.ecommerce.domain.coin.service.CoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<ListUpbitCoinResponseDto> gets() {
        ListUpbitCoinResponseDto responseDto = coinService.gets();
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/search")
    public ResponseEntity<ListUpbitCoinResponseDto> search(@RequestParam String query) {
        ListUpbitCoinResponseDto responseDto = coinService.search(query);
        return ResponseEntity.ok(responseDto);
    }
}
