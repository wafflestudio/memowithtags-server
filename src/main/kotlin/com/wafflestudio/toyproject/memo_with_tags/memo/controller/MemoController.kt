package com.wafflestudio.toyproject.memo_with_tags.memo.controller

import com.wafflestudio.toyproject.memo_with_tags.memo.service.MemoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MenuController(
    private val memoService: MemoService
) {

}

enum class SortType {
    ByRating,
    ByNumberOfReviews,
}
