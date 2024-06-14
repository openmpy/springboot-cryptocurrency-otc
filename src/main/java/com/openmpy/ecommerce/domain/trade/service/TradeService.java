package com.openmpy.ecommerce.domain.trade.service;

import com.openmpy.ecommerce.domain.coin.entity.CoinEntity;
import com.openmpy.ecommerce.domain.coin.repository.CoinRepository;
import com.openmpy.ecommerce.domain.member.entity.MemberEntity;
import com.openmpy.ecommerce.domain.member.repository.MemberRepository;
import com.openmpy.ecommerce.domain.trade.dto.request.BuyTradeRequestDto;
import com.openmpy.ecommerce.domain.trade.dto.request.SellTradeRequestDto;
import com.openmpy.ecommerce.domain.trade.dto.response.BuyTradeResponseDto;
import com.openmpy.ecommerce.domain.trade.dto.response.GetTradeResponseDto;
import com.openmpy.ecommerce.domain.trade.dto.response.SellTradeResponseDto;
import com.openmpy.ecommerce.domain.trade.entity.TradeEntity;
import com.openmpy.ecommerce.domain.trade.entity.constants.TradeType;
import com.openmpy.ecommerce.domain.trade.repository.TradeRepository;
import com.openmpy.ecommerce.domain.wallet.entity.WalletEntity;
import com.openmpy.ecommerce.domain.wallet.repository.WalletRepository;
import com.openmpy.ecommerce.global.exception.CustomException;
import com.openmpy.ecommerce.global.exception.constants.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class TradeService {

    private final TradeRepository tradeRepository;
    private final MemberRepository memberRepository;
    private final CoinRepository coinRepository;
    private final WalletRepository walletRepository;

    public BuyTradeResponseDto buy(String email, BuyTradeRequestDto requestDto) {
        MemberEntity memberEntity = validateMemberEntity(email);
        CoinEntity coinEntity = validateCoinEntity(requestDto.coinId());

        BigDecimal totalPrice = requestDto.amount().multiply(requestDto.price());
        if (memberEntity.getBalance().compareTo(totalPrice) < 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        TradeEntity tradeEntity = tradeRepository.save(requestDto.buy(memberEntity, coinEntity));
        memberEntity.minusBalance(totalPrice);
        return new BuyTradeResponseDto(tradeEntity);
    }

    public SellTradeResponseDto sell(String email, SellTradeRequestDto requestDto) {
        MemberEntity memberEntity = validateMemberEntity(email);
        CoinEntity coinEntity = validateCoinEntity(requestDto.coinId());

        WalletEntity walletEntity = walletRepository.findByMemberEntityAndCoinEntity(memberEntity, coinEntity)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_WALLET));

        if (walletEntity.getAmount().compareTo(requestDto.amount()) < 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        TradeEntity tradeEntity = tradeRepository.save(requestDto.sell(memberEntity, coinEntity));
        walletEntity.minusAmount(requestDto.amount());
        return new SellTradeResponseDto(tradeEntity);
    }

    public void cancel(String email, Long tradeId) {
        MemberEntity memberEntity = validateMemberEntity(email);
        TradeEntity tradeEntity = validateTradeEntity(tradeId);

        if (!memberEntity.equals(tradeEntity.getMemberEntity())) {
            throw new CustomException(ErrorCode.NO_MATCHES_TRADE_MEMBER);
        }

        cancelTradeType(tradeEntity, memberEntity);
        tradeRepository.delete(tradeEntity);
    }

    @Transactional(readOnly = true)
    public Page<GetTradeResponseDto> gets(String type, int page, int size) {
        PageRequest pageRequest = PageRequest.of(Math.max(0, page), size, Sort.Direction.DESC, "createdAt");
        Page<TradeEntity> pagedTrades = getPagedTradesByType(type, pageRequest);
        List<GetTradeResponseDto> tradeResponses = getTradeResponseDtos(pagedTrades);
        return new PageImpl<>(tradeResponses, pageRequest, pagedTrades.getTotalElements());
    }

    public void transaction(String email, Long tradeId) {
        MemberEntity memberEntity = validateMemberEntity(email);
        TradeEntity tradeEntity = validateTradeEntity(tradeId);

        if (tradeEntity.getTradeType().equals(TradeType.BUY)) {
            WalletEntity walletEntity = walletRepository.findByMemberEntityAndCoinEntity(memberEntity, tradeEntity.getCoinEntity())
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_WALLET));

            if (walletEntity.getAmount().compareTo(tradeEntity.getAmount()) < 0) {
                throw new CustomException(ErrorCode.INSUFFICIENT_BALANCE);
            }

            // 나
            walletEntity.minusAmount(tradeEntity.getAmount());
            memberEntity.plusBalance(tradeEntity.getAmount().multiply(tradeEntity.getPrice()));

            // 상대
            MemberEntity oppositeMemberEntity = tradeEntity.getMemberEntity();
            walletRepository.findByMemberEntityAndCoinEntity(oppositeMemberEntity, tradeEntity.getCoinEntity())
                    .ifPresentOrElse(
                            oppositeWalletEntity -> {
                                BigDecimal currentTotalAmount = oppositeWalletEntity.getAmount();
                                BigDecimal currentAverage = oppositeWalletEntity.getAverage();
                                BigDecimal newTradeAmount = tradeEntity.getAmount();
                                BigDecimal newTradePrice = tradeEntity.getPrice();
                                BigDecimal newAverage = (currentAverage.multiply(currentTotalAmount).add(newTradePrice.multiply(newTradeAmount)))
                                        .divide(currentTotalAmount.add(newTradeAmount), 10, RoundingMode.UNNECESSARY);

                                oppositeWalletEntity.plusAmount(tradeEntity.getAmount());
                                oppositeWalletEntity.updateAverage(newAverage);
                            },
                            () -> {
                                WalletEntity oppositeWalletEntity = WalletEntity.builder()
                                        .amount(tradeEntity.getAmount())
                                        .average(tradeEntity.getPrice())
                                        .memberEntity(oppositeMemberEntity)
                                        .coinEntity(tradeEntity.getCoinEntity())
                                        .build();

                                walletRepository.save(oppositeWalletEntity);
                            }
                    );
        } else if (tradeEntity.getTradeType().equals(TradeType.SELL)) {
            BigDecimal totalPrice = tradeEntity.getAmount().multiply(tradeEntity.getPrice());
            if (memberEntity.getBalance().compareTo(totalPrice) < 0) {
                throw new CustomException(ErrorCode.INSUFFICIENT_BALANCE);
            }

            // 나
            memberEntity.minusBalance(totalPrice);
            walletRepository.findByMemberEntityAndCoinEntity(memberEntity, tradeEntity.getCoinEntity())
                    .ifPresentOrElse(
                            walletEntity -> {
                                BigDecimal currentTotalAmount = walletEntity.getAmount();
                                BigDecimal currentAverage = walletEntity.getAverage();
                                BigDecimal newTradeAmount = tradeEntity.getAmount();
                                BigDecimal newTradePrice = tradeEntity.getPrice();
                                BigDecimal newAverage = (currentAverage.multiply(currentTotalAmount).add(newTradePrice.multiply(newTradeAmount)))
                                        .divide(currentTotalAmount.add(newTradeAmount), 10, RoundingMode.UNNECESSARY);

                                walletEntity.plusAmount(tradeEntity.getAmount());
                                walletEntity.updateAverage(newAverage);

                                walletEntity.plusAmount(tradeEntity.getAmount());
                                walletEntity.updateAverage(newAverage);
                            },
                            () -> {
                                WalletEntity walletEntity = WalletEntity.builder()
                                        .amount(tradeEntity.getAmount())
                                        .average(tradeEntity.getPrice())
                                        .memberEntity(memberEntity)
                                        .coinEntity(tradeEntity.getCoinEntity())
                                        .build();

                                walletRepository.save(walletEntity);
                            }
                    );

            // 상대
            MemberEntity oppositeMemberEntity = tradeEntity.getMemberEntity();
            oppositeMemberEntity.plusBalance(tradeEntity.getAmount().multiply(tradeEntity.getPrice()));
        }
        tradeRepository.delete(tradeEntity);
    }

    private TradeEntity validateTradeEntity(Long tradeId) {
        return tradeRepository.findById(tradeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TRADE));
    }

    private MemberEntity validateMemberEntity(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    private CoinEntity validateCoinEntity(Long coinId) {
        return coinRepository.findById(coinId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COIN));
    }

    private void cancelTradeType(TradeEntity tradeEntity, MemberEntity memberEntity) {
        if (tradeEntity.getTradeType().equals(TradeType.BUY)) {
            BigDecimal totalPrice = tradeEntity.getAmount().multiply(tradeEntity.getPrice());
            memberEntity.plusBalance(totalPrice);
        } else if (tradeEntity.getTradeType().equals(TradeType.SELL)) {
            walletRepository.findByMemberEntityAndCoinEntity(memberEntity, tradeEntity.getCoinEntity())
                    .ifPresentOrElse(
                            walletEntity -> walletEntity.plusAmount(tradeEntity.getAmount()),
                            () -> {
                                WalletEntity walletEntity = WalletEntity.builder()
                                        .amount(tradeEntity.getAmount())
                                        .average(BigDecimal.ZERO)
                                        .memberEntity(tradeEntity.getMemberEntity())
                                        .coinEntity(tradeEntity.getCoinEntity())
                                        .build();

                                walletRepository.save(walletEntity);
                            }
                    );
        }
    }

    private Page<TradeEntity> getPagedTradesByType(String type, PageRequest pageRequest) {
        Page<TradeEntity> pagedTrades;
        switch (type) {
            case "buy" -> pagedTrades = tradeRepository.findAllByTradeType(TradeType.BUY, pageRequest);
            case "sell" -> pagedTrades = tradeRepository.findAllByTradeType(TradeType.SELL, pageRequest);
            default -> pagedTrades = tradeRepository.findAll(pageRequest);
        }
        return pagedTrades;
    }

    private List<GetTradeResponseDto> getTradeResponseDtos(Page<TradeEntity> pagedTrades) {
        List<GetTradeResponseDto> tradeResponses = new ArrayList<>();
        pagedTrades.forEach(tradeEntity -> tradeResponses.add(new GetTradeResponseDto(tradeEntity)));
        return tradeResponses;
    }
}
