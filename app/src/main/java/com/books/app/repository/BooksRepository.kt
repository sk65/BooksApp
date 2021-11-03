package com.books.app.repository

import com.books.app.database.BooksDao
import com.books.app.model.response.Book
import com.books.app.model.response.TopBannerSlide
import com.books.app.model.response.YouWillLike
import javax.inject.Inject

class BooksRepository @Inject constructor(
    private val booksDao: BooksDao
) {
    suspend fun insertBook(book: Book) = booksDao.insertBook(book)

    suspend fun insertTopBannerSlide(topBannerSlide: TopBannerSlide) =
        booksDao.insertTopBannerSlide(topBannerSlide)

    suspend fun insertYouWillLike(youWillLike: YouWillLike) =
        booksDao.insertYouWillLike(youWillLike)

    suspend fun getBooksByGenre(genre: String): List<Book> = booksDao.getBooksByGenre(genre)
    suspend fun getAllBooks(): List<Book> = booksDao.getAllBooks()
    suspend fun getBookById(id: Long): Book = booksDao.getBookById(id)
    suspend fun getTopBannerSlides() = booksDao.getTopBannerSlides()
    suspend fun getYouWillAlsoLike() = booksDao.getYouWillLike()

    suspend fun clearDB() {
        booksDao.deleteAllBooks()
        booksDao.deleteAllTopBannerSlide()
        booksDao.deleteAllYouWillLike()
    }
}