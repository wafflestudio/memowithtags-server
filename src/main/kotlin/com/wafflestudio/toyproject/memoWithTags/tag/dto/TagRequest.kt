package com.wafflestudio.toyproject.memoWithTags.tag.dto

sealed class TagRequest {
    data class CreateTagRequest(
        val name: String,
        val colorHex: String
    ) : TagRequest()

    data class UpdateTagRequest(
        val name: String,
        val colorHex: String
    ) : TagRequest()
}
