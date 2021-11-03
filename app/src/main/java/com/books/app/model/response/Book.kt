package com.books.app.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @PrimaryKey
    val id: Long,
    val author: String,
    val cover_url: String,
    val genre: String,
    val likes: String,
    val name: String,
    val quotes: String,
    val summary: String,
    val views: String
)