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
    INVALID_POST_MEMBER(HttpStatus.BAD_REQUEST, "P-002", "본인이 작성한 게시글이 아닙니다."),
    TOO_MANY_POST_IMAGE(HttpStatus.BAD_REQUEST, "P-003", "이미지는 최대 3장까지 가능합니다."),
    TOO_LONG_POST_IMAGE_SIZE(HttpStatus.BAD_REQUEST, "P-004", "이미지 사이즈는 최대 5MB까지 가능합니다."),
    INVALID_POST_IMAGE_TYPE(HttpStatus.BAD_REQUEST, "P-005", "이미지만 업로드 할 수 있습니다."),
    UNKNOWN_POST_IMAGE_UPLOAD(HttpStatus.BAD_REQUEST, "P-006", "이미지 업로드 중 알 수 없는 에러가 발생했습니다."),
    ALREADY_LIKE_POST(HttpStatus.CONFLICT, "P-007", "이미 좋아요를 누른 게시글입니다."),
    ALREADY_REPORT_POST(HttpStatus.CONFLICT, "P-008", "이미 신고한 게시글입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
