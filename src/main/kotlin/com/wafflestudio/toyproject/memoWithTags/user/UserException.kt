package com.wafflestudio.toyproject.memoWithTags.user

sealed class UserException : RuntimeException()

class EmailNotFoundException : UserException()

class EmailVerificationFailureException : UserException()

class SignInInvalidPasswordException : UserException()

class AuthenticationFailedException : UserException()
