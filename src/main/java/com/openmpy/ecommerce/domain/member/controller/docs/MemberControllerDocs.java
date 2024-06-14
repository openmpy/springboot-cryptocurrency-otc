package com.openmpy.ecommerce.domain.member.controller.docs;

import com.openmpy.ecommerce.domain.member.dto.request.SigninMemberRequestDto;
import com.openmpy.ecommerce.domain.member.dto.request.SignupMemberRequestDto;
import com.openmpy.ecommerce.domain.member.dto.response.GetMemberResponseDto;
import com.openmpy.ecommerce.domain.member.dto.response.SigninMemberResponseDto;
import com.openmpy.ecommerce.domain.member.dto.response.SignupMemberResponseDto;
import com.openmpy.ecommerce.domain.wallet.dto.response.ListWalletResponseDto;
import com.openmpy.ecommerce.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "회원", description = "회원 관련 API")
public interface MemberControllerDocs {

    @Operation(summary = "회원가입 기능", description = "회원가입 API")
    ResponseEntity<SignupMemberResponseDto> signup(@Valid @RequestBody SignupMemberRequestDto requestDto);

    @Operation(summary = "로그인 기능", description = "로그인 API")
    ResponseEntity<SigninMemberResponseDto> signin(
            @Valid @RequestBody SigninMemberRequestDto requestDto,
            HttpServletRequest httpServletRequest
    );

    @Operation(summary = "회원 정보 조회 기능", description = "회원 정보 조회 API")
    ResponseEntity<GetMemberResponseDto> get(@AuthenticationPrincipal UserDetailsImpl userDetails);

    @Operation(summary = "회원 지갑 목록 조회 기능", description = "회원 지갑 목록 조회 API")
    ResponseEntity<ListWalletResponseDto> getWallets(@AuthenticationPrincipal UserDetailsImpl userDetails);
}
