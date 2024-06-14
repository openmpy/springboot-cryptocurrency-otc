package com.openmpy.ecommerce.domain.coin.controller.docs;

import com.openmpy.ecommerce.domain.coin.dto.response.GetUpbitCoinResponseDto;
import com.openmpy.ecommerce.domain.coin.dto.response.ListUpbitCoinResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "암호화폐", description = "암호화폐 관련 API")
public interface CoinControllerDocs {

    @Operation(summary = "암호화폐 정보 조회 기능", description = "암호화폐 정보 조회 API")
    ResponseEntity<GetUpbitCoinResponseDto> get(@PathVariable Long coinId);

    @Operation(summary = "암호화폐 목록 조회 기능", description = "암호화폐 목록 조회 API")
    ResponseEntity<ListUpbitCoinResponseDto> gets();

    @Operation(summary = "암호화폐 검색 기능", description = "암호화폐 검색 API")
    ResponseEntity<ListUpbitCoinResponseDto> search(@RequestParam String query);
}
