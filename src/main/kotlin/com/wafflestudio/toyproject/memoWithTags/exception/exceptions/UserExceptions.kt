package com.wafflestudio.toyproject.memoWithTags.exception.exceptions

import com.wafflestudio.toyproject.memoWithTags.exception.code.ErrorCode

class UserNotFoundException : CustomException(
    ErrorCode.USER_NOT_FOUND
)

// 회원가입 시 이메일이 이미 존재하는 경우
class EmailAlreadyExistsException : CustomException(
    ErrorCode.USER_EMAIL_ALREADY_EXISTS
)

// 인증 이메일을 보낼 수 없는 경우
class EmailSendingException : CustomException(
    ErrorCode.USER_UNABLE_TO_SEND_EMAIL
)

// 회원가입 시 메일 인증 과정을 거치지 않은 경우
class EmailNotVerifiedException : CustomException(
    ErrorCode.USER_EMAIL_NOT_VERIFIED
)

// 로그인 실패
class SignInInvalidException : CustomException(
    ErrorCode.USER_SIGN_IN_INVALID
)

// 잘못된 메일 인증 코드
class MailVerificationException : CustomException(
    ErrorCode.USER_MAIL_VERIFICATION_FAILED
)

// 인증 실패
class AuthenticationFailedException : CustomException(
    ErrorCode.USER_AUTHENTICATION_FAILED
)

// 소셜 로그인 요청 실패
class OAuthRequestException : CustomException(
    ErrorCode.USER_SOCIAL_LOGIN_FAILED
)

// 비밀번호 변경 시 잘못된 비밀번호 입력
class UpdatePasswordInvalidException : CustomException(
    ErrorCode.USER_UPDATE_PASSWORD_INVALID
)

// 회원 탈퇴 시 이메일을 잘못 입력
class EmailNotMatchException : CustomException(
    ErrorCode.USER_EMAIL_NOT_MATCHED
)

// 이메일 형식이 유효하지 않은 경우
class EmailNotValidException : CustomException(
    ErrorCode.USER_WRONG_EMAIL_FORMAT
)

// 비밀번호 형식이 유효하지 않은 경우
class PasswordNotValidException : CustomException(
    ErrorCode.USER_WRONG_PASSWORD_FORMAT
)

// 닉네임 형식이 유효하지 않은 경우
class NicknameNotValidException : CustomException(
    ErrorCode.USER_WRONG_NICKNAME_FORMAT
)

// Admin 권한 없음
class UserNotAdminException : CustomException(
    ErrorCode.USER_NOT_ADMIN
)
