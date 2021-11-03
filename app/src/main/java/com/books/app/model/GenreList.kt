package com.books.app.model

import com.books.app.model.response.Book

data class GenreList(
    val genre: String,
    val books: List<Book>
)
