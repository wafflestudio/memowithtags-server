package com.wafflestudio.toyproject.memoWithTags.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

sealed class UserException(
    httpErrorCode: HttpStatusCode,
    errorCode: Int,
    msg: String
) : CustomException(httpErrorCode, errorCode, msg)

// 회원가입 시 이메일이 이미 존재하는 경우
class EmailAlreadyExistsException : UserException(
    errorCode = 0,
    httpErrorCode = HttpStatus.BAD_REQUEST,
    msg = "이미 등록된 이메일입니다."
)


// 이메일을 찾을 수 없는 경우
class EmailNotFoundException : UserException(
    errorCode = 0,
    httpErrorCode = HttpStatus.NOT_FOUND,
    msg = "이메일을 찾을 수 없습니다."
)


// 잘못된 비밀번호로 로그인 시도
class SignInInvalidPasswordException : UserException(
    errorCode = 0,
    httpErrorCode = HttpStatus.BAD_REQUEST,
    msg = "잘못된 비밀번호입니다."
)

// 인증 실패
class AuthenticationFailedException : UserException(
    errorCode = 0,
    httpErrorCode = HttpStatus.UNAUTHORIZED,
    msg = "인증에 실패했습니다."
)
