package com.wafflestudio.toyproject.memoWithTags.exception


enum class ErrorCode(
    val status: Int,
    val code: String,
    val message: String
) {
    // User Exceptions
    USER_NOT_FOUND(404, "USER_NOT_FOUND", "존재하지 않는 유저입니다."),
    USER_EMAIL_ALREADY_EXISTS(409, "USER_EMAIL_ALREADY_EXISTS", "이미 존재하는 이메일입니다."),
    USER_UNABLE_TO_SEND_EMAIL(500, "USER_UNABLE_TO_SEND_EMAIL", "이메일 전송에 실패했습니다."),
    USER_EMAIL_NOT_VERIFIED(400, "USER_EMAIL_NOT_VERIFIED", "인증이 완료되지 않은 이메일입니다."),
    USER_SIGN_IN_INVALID(401, "USER_SIGN_IN_INVALID", "이메일이 존재하지 않거나 잘못된 비밀번호입니다."),
    USER_MAIL_VERIFICATION_FAILED(401, "USER_MAIL_VERIFICATION_FAILED", "잘못된 인증 코드입니다."),
    USER_AUTHENTICATION_FAILED(401, "USER_AUTHENTICATION_FAILED", "인증에 실패했습니다."),
    USER_SOCIAL_LOGIN_FAILED(400, "USER_SOCIAL_LOGIN_FAILED", "소셜 로그인 요청에 실패했습니다."),
    USER_UPDATE_PASSWORD_INVALID(403, "USER_UPDATE_PASSWORD_INVALID", "잘못된 비밀번호입니다."),
    USER_EMAIL_NOT_MATCHED(404, "USER_EMAIL_NOT_MATCHED", "이메일이 일치하지 않습니다."),
    USER_WRONG_EMAIL_FORMAT(400, "USER_WRONG_EMAIL_FORMAT", "잘못된 이메일 형식입니다."),
    USER_WRONG_PASSWORD_FORMAT(400, "USER_WRONG_PASSWORD_FORMAT", "잘못된 비밀번호 형식입니다."),
    USER_WRONG_NICKNAME_FORMAT(400, "USER_WRONG_NICKNAME_FORMAT", "잘못된 닉네임 형식입니다."),
    USER_NOT_ADMIN(403, "USER_NOT_ADMIN", "Admin 권한이 없습니다"),

    // Memo Exceptions
    MEMO_NOT_FOUND(404, "MEMO_NOT_FOUND", "존재하지 않는 메모입니다."),
    MEMO_ACCESS_DENIED(403, "MEMO_ACCESS_DENIED", "해당 메모를 업데이트할 권한이 없습니다."),

    // Tag Exceptions
    TAG_NOT_FOUND(404, "TAG_NOT_FOUND", "존재하지 않는 태그입니다."),
    TAG_NOT_OWNED_BY_USER(403, "TAG_NOT_OWNED_BY_USER", "유저가 가지고 있는 태그가 아닙니다."),

    // Token Exceptions
    TOKEN_EXPIRED(401, "TOKEN_EXPIRED", "토큰이 만료되었습니다."),
    TOKEN_INVALID_SIGNATURE(401, "TOKEN_INVALID_SIGNATURE", "토큰의 서명이 유효하지 않습니다."),
    TOKEN_INVALID(401, "TOKEN_INVALID", "토큰이 유효하지 않습니다."),
}