package com.wafflestudio.toyproject.memoWithTags.tag

sealed class TagException : RuntimeException()

class TagNotFoundException : TagException()
class WrongUserException : TagException()
