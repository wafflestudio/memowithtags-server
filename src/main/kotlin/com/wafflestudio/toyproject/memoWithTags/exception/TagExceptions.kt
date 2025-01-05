package com.wafflestudio.toyproject.memoWithTags.exception


class TagNotFoundException : CustomException(
    "TAG_NOT_FOUND",
    "태그를 찾을 수 없습니다",
)
class WrongUserException : CustomException(
    "WRONG_USER_EXCEPTION",
    "잘못된 유저입니다",
)
