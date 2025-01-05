package com.wafflestudio.toyproject.memoWithTags.tag.persistence

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.*

@Entity
class TagEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: Long? = null,
)
