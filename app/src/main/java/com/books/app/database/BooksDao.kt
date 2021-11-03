package com.books.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.books.app.model.response.Book
import com.books.app.model.response.TopBannerSlide
import com.books.app.model.response.YouWillLike

@Dao
interface BooksDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertBook(book: Book)

    @Insert(onConflict = REPLACE)
    suspend fun insertTopBannerSlide(topBannerSlide: TopBannerSlide)

    @Insert(onConflict = REPLACE)
    suspend fun insertYouWillLike(youWillLike: YouWillLike)

    @Query("SELECT * FROM book where genre =:genre")
    suspend fun getBooksByGenre(genre: String): List<Book>

    @Query("SELECT * FROM book where id =:id")
    suspend fun getBookById(id: Long): Book

    @Query("SELECT * FROM youwilllike")
    suspend fun getYouWillLike(): List<YouWillLike>

    @Query("DELETE FROM youwilllike")
    suspend fun deleteAllYouWillLike()

    @Query("DELETE FROM book")
    suspend fun deleteAllBooks()

    @Query("DELETE FROM topbannerslide")
    suspend fun deleteAllTopBannerSlide()

    @Query("SELECT * FROM book")
    suspend fun getAllBooks(): List<Book>

    @Query("SELECT * FROM topbannerslide")
    suspend fun getTopBannerSlides(): List<TopBannerSlide>
}