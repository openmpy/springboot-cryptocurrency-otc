package com.openmpy.ecommerce.domain.member.controller;

import com.openmpy.ecommerce.domain.member.controller.docs.MemberControllerDocs;
import com.openmpy.ecommerce.domain.member.dto.request.SigninMemberRequestDto;
import com.openmpy.ecommerce.domain.member.dto.request.SignupMemberRequestDto;
import com.openmpy.ecommerce.domain.member.dto.response.GetMemberResponseDto;
import com.openmpy.ecommerce.domain.member.dto.response.SigninMemberResponseDto;
import com.openmpy.ecommerce.domain.member.dto.response.SignupMemberResponseDto;
import com.openmpy.ecommerce.domain.member.service.MemberService;
import com.openmpy.ecommerce.domain.wallet.dto.response.ListWalletResponseDto;
import com.openmpy.ecommerce.domain.wallet.service.WalletService;
import com.openmpy.ecommerce.global.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@RestController
public class MemberController implements MemberControllerDocs {

    private final MemberService memberService;
    private final WalletService walletService;

    @PostMapping("/signup")
    public ResponseEntity<SignupMemberResponseDto> signup(
            @Valid @RequestBody SignupMemberRequestDto requestDto
    ) {
        SignupMemberResponseDto responseDto = memberService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/signin")
    public ResponseEntity<SigninMemberResponseDto> signin(
            @Valid @RequestBody SigninMemberRequestDto requestDto,
            HttpServletRequest httpServletRequest
    ) {
        SigninMemberResponseDto responseDto = memberService.signin(requestDto, httpServletRequest);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<GetMemberResponseDto> get(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String email = userDetails.getUsername();
        GetMemberResponseDto responseDto = memberService.get(email);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/wallets")
    public ResponseEntity<ListWalletResponseDto> getWallets(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String email = userDetails.getUsername();
        ListWalletResponseDto responseDto = walletService.get(email);
        return ResponseEntity.ok(responseDto);
    }
}
