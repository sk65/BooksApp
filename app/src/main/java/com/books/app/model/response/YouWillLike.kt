package com.books.app.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class YouWillLike(
    @PrimaryKey(autoGenerate = true)
    val youWillLikeId: Long?,
    val bookId: Long
)
