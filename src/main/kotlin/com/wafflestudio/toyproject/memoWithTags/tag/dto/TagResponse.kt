package com.wafflestudio.toyproject.memoWithTags.tag.dto

sealed class TagResponse {
    data class UpdateTagRequest(
        val name: String,
        val color: String
    ) : TagResponse()
}
