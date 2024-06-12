package com.openmpy.ecommerce.domain.member.controller;

import com.openmpy.ecommerce.domain.member.dto.request.SigninMemberRequestDto;
import com.openmpy.ecommerce.domain.member.dto.request.SignupMemberRequestDto;
import com.openmpy.ecommerce.domain.member.dto.response.GetMemberResponseDto;
import com.openmpy.ecommerce.domain.member.dto.response.SigninMemberResponseDto;
import com.openmpy.ecommerce.domain.member.dto.response.SignupMemberResponseDto;
import com.openmpy.ecommerce.domain.member.service.MemberService;
import com.openmpy.ecommerce.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<SignupMemberResponseDto> signup(
            @Valid @RequestBody SignupMemberRequestDto requestDto
    ) {
        SignupMemberResponseDto responseDto = memberService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/signin")
    public ResponseEntity<SigninMemberResponseDto> signin(
            @Valid @RequestBody SigninMemberRequestDto requestDto
    ) {
        SigninMemberResponseDto responseDto = memberService.signin(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<GetMemberResponseDto> get(Authentication authentication) {
        String email = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        GetMemberResponseDto responseDto = memberService.get(email);
        return ResponseEntity.ok(responseDto);
    }
}
