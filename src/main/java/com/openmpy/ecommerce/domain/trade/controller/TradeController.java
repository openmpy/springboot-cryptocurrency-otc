package com.openmpy.ecommerce.domain.trade.controller;

import com.openmpy.ecommerce.domain.trade.dto.request.BuyTradeRequestDto;
import com.openmpy.ecommerce.domain.trade.dto.request.SellTradeRequestDto;
import com.openmpy.ecommerce.domain.trade.dto.response.BuyTradeResponseDto;
import com.openmpy.ecommerce.domain.trade.dto.response.SellTradeResponseDto;
import com.openmpy.ecommerce.domain.trade.service.TradeService;
import com.openmpy.ecommerce.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/trades")
@RestController
public class TradeController {

    private final TradeService tradeService;

    @PostMapping("/buy")
    public ResponseEntity<BuyTradeResponseDto> buy(
            @Valid @RequestBody BuyTradeRequestDto requestDto,
            Authentication authentication
    ) {
        String email = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        BuyTradeResponseDto responseDto = tradeService.buy(requestDto, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/sell")
    public ResponseEntity<SellTradeResponseDto> sell(
            @Valid @RequestBody SellTradeRequestDto requestDto,
            Authentication authentication
    ) {
        String email = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        SellTradeResponseDto responseDto = tradeService.sell(requestDto, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @DeleteMapping("/{tradeId}")
    public ResponseEntity<Void> cancel(
            @PathVariable Long tradeId,
            Authentication authentication
    ) {
        String email = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        tradeService.cancel(tradeId, email);
        return ResponseEntity.noContent().build();
    }
}
