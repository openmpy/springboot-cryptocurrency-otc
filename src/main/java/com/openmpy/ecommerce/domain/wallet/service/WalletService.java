package com.openmpy.ecommerce.domain.wallet.service;

import com.openmpy.ecommerce.domain.member.entity.MemberEntity;
import com.openmpy.ecommerce.domain.member.repository.MemberRepository;
import com.openmpy.ecommerce.domain.wallet.dto.response.GetWalletResponseDto;
import com.openmpy.ecommerce.domain.wallet.dto.response.ListWalletResponseDto;
import com.openmpy.ecommerce.domain.wallet.repository.WalletRepository;
import com.openmpy.ecommerce.global.exception.CustomException;
import com.openmpy.ecommerce.global.exception.constants.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final MemberRepository memberRepository;

    public ListWalletResponseDto get(String email) {
        MemberEntity memberEntity = validateMemberEntity(email);

        List<GetWalletResponseDto> walletResponses = walletRepository.findAllByMemberEntity(memberEntity).stream()
                .map(GetWalletResponseDto::new)
                .toList();

        return new ListWalletResponseDto(walletResponses);
    }

    private MemberEntity validateMemberEntity(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }
}
