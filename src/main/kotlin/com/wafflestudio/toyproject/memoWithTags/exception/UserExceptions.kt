package com.wafflestudio.toyproject.memoWithTags.exception

sealed class UserException(
    errorCode: String,
    message: String
) : CustomException(errorCode, message)

// 이메일을 찾을 수 없는 경우
class EmailNotFoundException : UserException(
    errorCode = "EMAIL_NOT_FOUND",
    message = "이메일을 찾을 수 없습니다."
)

// 이메일 인증 실패
class EmailVerificationFailureException : UserException(
    errorCode = "EMAIL_VERIFICATION_FAILED",
    message = "이메일 인증에 실패했습니다."
)

// 잘못된 비밀번호로 로그인 시도
class SignInInvalidPasswordException : UserException(
    errorCode = "INVALID_PASSWORD",
    message = "잘못된 비밀번호입니다."
)

// 인증 실패
class AuthenticationFailedException : UserException(
    errorCode = "AUTHENTICATION_FAILED",
    message = "인증에 실패했습니다."
)
