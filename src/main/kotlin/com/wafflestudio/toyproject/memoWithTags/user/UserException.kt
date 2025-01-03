package com.wafflestudio.toyproject.memoWithTags.user

sealed class UserException : RuntimeException()

class EmailNotFoundException : UserException()