package com.wafflestudio.toyproject.memoWithTags.tag.dto

sealed class TagRequest {
    data class CreateTagRequest(
        val name: String,
        val color: String
    ) : TagRequest()
}
