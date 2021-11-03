package com.books.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.books.app.model.GenreList
import com.books.app.model.response.Book
import com.books.app.model.response.TopBannerSlide
import com.books.app.repository.BooksRepository
import com.books.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject
constructor(
    private val booksRepository: BooksRepository,
) : ViewModel() {
    private val _topBannerSlideResource = MutableLiveData<Resource<List<TopBannerSlide>>>()
    val topBannerSlideResource: LiveData<Resource<List<TopBannerSlide>>> = _topBannerSlideResource

    private val _genreListsResource = MutableLiveData<Resource<List<GenreList>>>()
    val genreListsResource: LiveData<Resource<List<GenreList>>> = _genreListsResource


    init {
        fetchTopBannerSlides()
        fetchGenreLists()
    }

    private fun fetchGenreLists() {
        viewModelScope.launch {
            _genreListsResource.postValue(Resource.Loading())
            val books = HashMap<String, MutableList<Book>>()
            booksRepository.getAllBooks().forEach { book ->
                if (books.containsKey(book.genre)) {
                    books[book.genre]?.add(book)
                } else {
                    books[book.genre] = ArrayList<Book>().apply {
                        add(book)
                    }
                }
            }
            val genreList: MutableList<GenreList> = ArrayList()
            books.keys.forEach { key ->
                genreList.add(GenreList(key, books[key]!!.toList()))
            }
            _genreListsResource.postValue(Resource.Success(genreList))
        }
    }

    private fun fetchTopBannerSlides() {
        viewModelScope.launch {
            _topBannerSlideResource.postValue(Resource.Loading())
            _topBannerSlideResource.postValue(Resource.Success(booksRepository.getTopBannerSlides()))
        }
    }
}