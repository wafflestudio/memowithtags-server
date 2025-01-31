package com.wafflestudio.toyproject.memoWithTags

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class MemoWithTagsApplication

fun main(args: Array<String>) {
    runApplication<MemoWithTagsApplication>(*args)
}
