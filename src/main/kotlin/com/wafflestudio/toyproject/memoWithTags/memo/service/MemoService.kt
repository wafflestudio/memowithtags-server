package com.wafflestudio.toyproject.memoWithTags.memo.service

import com.wafflestudio.toyproject.memoWithTags.memo.controller.Memo
import com.wafflestudio.toyproject.memoWithTags.memo.persistence.MemoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemoService(
    private val memoRepository: MemoRepository
){
    @Transactional
    fun createMemo(memo: Memo){

    }
}
