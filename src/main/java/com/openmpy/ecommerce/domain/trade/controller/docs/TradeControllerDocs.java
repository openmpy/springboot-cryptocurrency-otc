package com.openmpy.ecommerce.domain.trade.controller.docs;

import com.openmpy.ecommerce.domain.trade.dto.request.BuyTradeRequestDto;
import com.openmpy.ecommerce.domain.trade.dto.request.SellTradeRequestDto;
import com.openmpy.ecommerce.domain.trade.dto.response.BuyTradeResponseDto;
import com.openmpy.ecommerce.domain.trade.dto.response.GetTradeResponseDto;
import com.openmpy.ecommerce.domain.trade.dto.response.SellTradeResponseDto;
import com.openmpy.ecommerce.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "거래", description = "거래 관련 API")
public interface TradeControllerDocs {

    @Operation(summary = "거래 매수 등록 기능", description = "거래 매수 등록 API")
    ResponseEntity<BuyTradeResponseDto> buy(
            @Valid @RequestBody BuyTradeRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    @Operation(summary = "거래 매도 등록 기능", description = "거래 매도 등록 API")
    ResponseEntity<SellTradeResponseDto> sell(
            @Valid @RequestBody SellTradeRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    @Operation(summary = "거래 취소 기능", description = "거래 취소 API")
    ResponseEntity<Void> cancel(
            @PathVariable Long tradeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    @Operation(summary = "거래 목록 조회 기능", description = "거래 목록 조회 API")
    ResponseEntity<Page<GetTradeResponseDto>> gets(
            @RequestParam(defaultValue = "all") String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "거래 체결 기능", description = "거래 체결 API")
    ResponseEntity<Void> transaction(
            @PathVariable Long tradeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    );
}
