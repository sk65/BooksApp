package com.books.app.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.books.app.model.response.Book
import com.books.app.model.response.TopBannerSlide
import com.books.app.model.response.YouWillLike

@Database(
    entities = [
        Book::class,
        TopBannerSlide::class,
        YouWillLike::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BooksDatabase : RoomDatabase() {
    abstract fun booksDao(): BooksDao
}
