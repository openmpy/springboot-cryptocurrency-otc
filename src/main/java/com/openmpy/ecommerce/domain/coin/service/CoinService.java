package com.openmpy.ecommerce.domain.coin.service;

import com.openmpy.ecommerce.domain.coin.dto.response.GetUpbitCoinResponseDto;
import com.openmpy.ecommerce.domain.coin.dto.response.ListUpbitCoinResponseDto;
import com.openmpy.ecommerce.domain.coin.entity.CoinEntity;
import com.openmpy.ecommerce.domain.coin.repository.CoinRepository;
import com.openmpy.ecommerce.global.exception.CustomException;
import com.openmpy.ecommerce.global.exception.constants.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CoinService {

    private final CoinRepository coinRepository;

    public GetUpbitCoinResponseDto get(Long coinId) {
        CoinEntity coinEntity = coinRepository.findById(coinId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COIN));

        return new GetUpbitCoinResponseDto(coinEntity);
    }

    public ListUpbitCoinResponseDto gets() {
        List<GetUpbitCoinResponseDto> responseDtos = coinRepository.findAll().stream()
                .map(GetUpbitCoinResponseDto::new)
                .toList();
        return new ListUpbitCoinResponseDto(responseDtos);
    }

    public ListUpbitCoinResponseDto search(String query) {
        List<GetUpbitCoinResponseDto> responseDtos = coinRepository.findByQuery(query).stream()
                .map(GetUpbitCoinResponseDto::new)
                .toList();
        return new ListUpbitCoinResponseDto(responseDtos);
    }
}
