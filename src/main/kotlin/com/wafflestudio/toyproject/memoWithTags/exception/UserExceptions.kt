package com.wafflestudio.toyproject.memoWithTags.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

sealed class UserException(
    httpErrorCode: HttpStatusCode,
    errorCode: Int,
    msg: String
) : CustomException(httpErrorCode, errorCode, msg)

class UserNotFoundException : UserException(
    errorCode = 0,
    httpErrorCode = HttpStatus.NOT_FOUND,
    msg = "해당 이메일의 유저가 존재하지 않습니다."
)

// 회원가입 시 이메일이 이미 존재하는 경우
class EmailAlreadyExistsException : UserException(
    errorCode = 0,
    httpErrorCode = HttpStatus.CONFLICT,
    msg = "이미 등록된 이메일입니다."
)

// 인증 이메일을 보낼 수 없는 경우
class EmailSendingException : UserException(
    errorCode = 0,
    httpErrorCode = HttpStatus.INTERNAL_SERVER_ERROR,
    msg = "인증 이메일 전송에 실패했습니다."
)

// 회원가입 시 메일 인증 과정을 거치지 않은 경우
class EmailNotVerifiedException : UserException(
    errorCode = 0,
    httpErrorCode = HttpStatus.BAD_REQUEST,
    msg = "인증이 완료되지 않은 이메일입니다."
)

// 로그인 실패
class SignInInvalidException : UserException(
    errorCode = 0,
    httpErrorCode = HttpStatus.UNAUTHORIZED,
    msg = "이메일이 존재하지 않거나 잘못된 비밀번호입니다."
)

// 잘못된 메일 인증 코드
class MailVerificationException : UserException(
    errorCode = 0,
    httpErrorCode = HttpStatus.UNAUTHORIZED,
    msg = "잘못된 인증 코드입니다."
)

// 인증 실패
class AuthenticationFailedException : UserException(
    errorCode = 0,
    httpErrorCode = HttpStatus.UNAUTHORIZED,
    msg = "인증에 실패했습니다."
)

// 소셜 로그인 요청 실패
class OAuthRequestException : UserException(
    errorCode = 0,
    httpErrorCode = HttpStatus.BAD_REQUEST,
    msg = "소셜 로그인 요청에 실패했습니다."
)

// 비밀번호 변경 시 잘못된 비밀번호 입력
class UpdatePasswordInvalidException : UserException(
    errorCode = 0,
    httpErrorCode = HttpStatus.FORBIDDEN,
    msg = "잘못된 비밀번호입니다."
)

// 회원 탈퇴 시 이메일을 잘못 입력
class EmailNotMatchException : UserException(
    errorCode = 0,
    httpErrorCode = HttpStatus.NOT_FOUND,
    msg = "입력한 이메일이 유저의 이메일과 일치하지 않습니다"
)

// 이메일 형식이 유효하지 않은 경우
class EmailNotValidException : UserException(
    errorCode = 0,
    httpErrorCode = HttpStatus.BAD_REQUEST,
    msg = "이메일 형식이 올바르지 않습니다"
)

// 비밀번호 형식이 유효하지 않은 경우
class PasswordNotValidException : UserException(
    errorCode = 0,
    httpErrorCode = HttpStatus.BAD_REQUEST,
    msg = "비밀번호 형식이 올바르지 않습니다"
)

// 닉네임 형식이 유효하지 않은 경우
class NicknameNotValidException : UserException(
    errorCode = 0,
    httpErrorCode = HttpStatus.BAD_REQUEST,
    msg = "닉네임 형식이 올바르지 않습니다"
)

// Admin 권한 없음
class UserNotAdminException : UserException(
    errorCode = 0,
    httpErrorCode = HttpStatus.BAD_REQUEST,
    msg = "Admin 권한이 없습니다"
)
