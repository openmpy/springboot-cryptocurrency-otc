package com.openmpy.ecommerce.domain.coin.scheduler;

import com.openmpy.ecommerce.domain.coin.dto.request.GetUpbitCoinRequestDto;
import com.openmpy.ecommerce.domain.coin.repository.CoinRepository;
import com.openmpy.ecommerce.global.exception.CustomException;
import com.openmpy.ecommerce.global.exception.constants.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Component
public class CoinScheduler {

    private static final String UPBIT_MARKET_API_URL = "https://api.upbit.com/v1/market/all";
    private static final Logger log = LoggerFactory.getLogger(CoinScheduler.class);

    private final RestClient restClient = RestClient.create();
    private final CoinRepository coinRepository;

    // 한시간 마다 실행
    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void fetchUpbitListingCoins() {
        GetUpbitCoinRequestDto[] requestDtos = fetchUpbitCoinRequestDtos();
        int fetchCount = 0;

        for (GetUpbitCoinRequestDto requestDto : requestDtos) {
            if (requestDto.market().startsWith("KRW-") && !coinRepository.existsByMarket(requestDto.market())) {
                coinRepository.save(requestDto.fetch());
                fetchCount++;
            }
        }
        log.info("Fetching upbit listing coins: {}", fetchCount);
    }

    private GetUpbitCoinRequestDto[] fetchUpbitCoinRequestDtos() {
        return restClient.get()
                .uri(UPBIT_MARKET_API_URL)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    throw new CustomException(ErrorCode.INVALID_FETCHES_UPBIT_COINS);
                }))
                .body(GetUpbitCoinRequestDto[].class);
    }
}
