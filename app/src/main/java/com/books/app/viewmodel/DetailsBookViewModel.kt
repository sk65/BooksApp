package com.books.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.books.app.model.response.Book
import com.books.app.repository.BooksRepository
import com.books.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsBookViewModel @Inject constructor(
    private val booksRepository: BooksRepository,
) : ViewModel() {
    private val _booksResource = MutableLiveData<Resource<List<Book>>>()
    val booksResource: LiveData<Resource<List<Book>>> = _booksResource

    private val _bookResource = MutableLiveData<Resource<Book>>()
    val bookResource: LiveData<Resource<Book>> = _bookResource

    private val _youWillLikeResource = MutableLiveData<Resource<List<Book>>>()
    val youWillLikeResource: LiveData<Resource<List<Book>>> = _youWillLikeResource

    init {
        fetchYouWillLike()
    }

    private fun fetchYouWillLike() {
        _youWillLikeResource.postValue(Resource.Loading())
        viewModelScope.launch {
            val youWillLYikes = booksRepository.getYouWillAlsoLike()
            val books = ArrayList<Book>()
            youWillLYikes.forEach {
                books.add(booksRepository.getBookById(it.bookId))
            }
            _youWillLikeResource.postValue(Resource.Success(books))
        }
    }

    fun fetchGenreList(genre: String) {
        _booksResource.postValue(Resource.Loading())
        viewModelScope.launch {
            val booksByGenre = booksRepository.getBooksByGenre(genre)
            _booksResource.postValue(Resource.Success(booksByGenre))
        }
    }

    fun fetchBookById(id: Long) {
        viewModelScope.launch {
            _bookResource.postValue(Resource.Success(booksRepository.getBookById(id)))
        }
    }
}