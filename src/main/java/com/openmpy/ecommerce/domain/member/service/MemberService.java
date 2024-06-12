package com.openmpy.ecommerce.domain.member.service;

import com.openmpy.ecommerce.domain.member.dto.request.SigninMemberRequestDto;
import com.openmpy.ecommerce.domain.member.dto.request.SignupMemberRequestDto;
import com.openmpy.ecommerce.domain.member.dto.response.GetMemberResponseDto;
import com.openmpy.ecommerce.domain.member.dto.response.SigninMemberResponseDto;
import com.openmpy.ecommerce.domain.member.dto.response.SignupMemberResponseDto;
import com.openmpy.ecommerce.domain.member.entity.MemberEntity;
import com.openmpy.ecommerce.domain.member.entity.MemberHistoryEntity;
import com.openmpy.ecommerce.domain.member.repository.MemberHistoryRepository;
import com.openmpy.ecommerce.domain.member.repository.MemberRepository;
import com.openmpy.ecommerce.global.exception.CustomException;
import com.openmpy.ecommerce.global.exception.constants.ErrorCode;
import com.openmpy.ecommerce.global.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MemberHistoryRepository memberHistoryRepository;

    public SignupMemberResponseDto signup(SignupMemberRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.email())) {
            throw new CustomException(ErrorCode.ALREADY_EXISTS_MEMBER);
        }

        String encodedPassword = passwordEncoder.encode(requestDto.password());
        MemberEntity memberEntity = memberRepository.save(requestDto.signup(encodedPassword));
        return new SignupMemberResponseDto(memberEntity);
    }

    public SigninMemberResponseDto signin(SigninMemberRequestDto requestDto, HttpServletRequest httpServletRequest) {
        MemberEntity memberEntity = validateMemberEntity(requestDto.email());

        if (!passwordEncoder.matches(requestDto.password(), memberEntity.getPassword())) {
            throw new CustomException(ErrorCode.NO_MATCHES_PASSWORD);
        }

        String accessToken = jwtService.generateAccessToken(memberEntity);
        getMemberIp(memberEntity, httpServletRequest);
        return new SigninMemberResponseDto(accessToken);
    }

    @Transactional(readOnly = true)
    public GetMemberResponseDto get(String email) {
        MemberEntity memberEntity = validateMemberEntity(email);

        return new GetMemberResponseDto(memberEntity);
    }

    private MemberEntity validateMemberEntity(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    private void getMemberIp(MemberEntity memberEntity, HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = httpServletRequest.getRemoteAddr();
        }

        MemberHistoryEntity memberHistoryEntity = MemberHistoryEntity.builder()
                .ip(ip)
                .memberEntity(memberEntity)
                .build();

        memberHistoryRepository.save(memberHistoryEntity);
    }
}
