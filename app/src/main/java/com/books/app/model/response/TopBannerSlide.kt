package com.books.app.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TopBannerSlide(
    @PrimaryKey
    val book_id: Long,
    val cover: String,
    val id: Long
)