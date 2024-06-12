package com.openmpy.ecommerce.global.exception.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S-001", "서버 에러가 발생했습니다."),
    VALIDATION_FAIL(HttpStatus.BAD_REQUEST, "S-002", "유효성 검사에 실패했습니다."),

    JWT_ERROR(HttpStatus.UNAUTHORIZED, "JWT-001", "JWT 토큰 에러가 발생했습니다."),

    ALREADY_EXISTS_MEMBER(HttpStatus.CONFLICT, "M-001", "이미 가입된 회원 계정입니다."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "M-002", "찾을 수 없는 회원 계정입니다."),
    NO_MATCHES_PASSWORD(HttpStatus.BAD_REQUEST, "M-003", "비밀번호가 일치하지 않습니다."),

    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "P-001", "찾을 수 없는 게시글입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
