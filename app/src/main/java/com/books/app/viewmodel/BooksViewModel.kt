package com.books.app.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.books.app.R
import com.books.app.model.GenreList
import com.books.app.model.response.Book
import com.books.app.model.response.Response
import com.books.app.model.response.TopBannerSlide
import com.books.app.model.response.YouWillLike
import com.books.app.repository.BooksRepository
import com.books.app.util.JSON_DATA_KEY
import com.books.app.util.Resource
import com.books.app.util.getMessageFromException
import com.books.app.util.hasInternetConnection
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject
constructor(
    @ApplicationContext val context: Context,
    private val remoteConfig: FirebaseRemoteConfig,
    private val booksRepository: BooksRepository,
    private val gson: Gson
) : ViewModel() {

    private var _resource = MutableLiveData<Resource<List<String>>>()
    val resource: LiveData<Resource<List<String>>> = _resource
    val genreListResource = MutableLiveData<Resource<List<Book>>>()
    val genreListsResource = MutableLiveData<Resource<List<GenreList>>>()
    val bookResource = MutableLiveData<Resource<Book>>()
    val youWillLikeResource = MutableLiveData<Resource<List<Book>>>()
    val topBannerSlideResource = MutableLiveData<Resource<List<TopBannerSlide>>>()

    init {
       // fetchData()
    }

    private fun fetchData() {
        _resource.postValue(Resource.Loading())
        if (hasInternetConnection(context)) {
            try {
                remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val asString = remoteConfig[JSON_DATA_KEY].asString()
                        val response = gson.fromJson(asString, Response::class.java)
                        saveDataToDB(response)
                    } else {
                        _resource.postValue(task.exception?.let {
                            Resource.Error(it.getMessageFromException(context))
                        })
                    }
                }
            } catch (e: Exception) {
                _resource.postValue(Resource.Error(e.getMessageFromException(context)))
            }
        } else {
            _resource.postValue(Resource.Error(context.getString(R.string.no_internet_connection)))
        }
    }

    fun fetchGenreLists() {
        viewModelScope.launch {
            val map = HashMap<String, MutableList<Book>>()
            booksRepository.getAllBooks().forEach { book ->
                if (map.containsKey(book.genre)) {
                    map[book.genre]?.add(book)
                } else {
                    map[book.genre] = ArrayList<Book>().apply {
                        add(book)
                    }
                }
            }
            val genreList: MutableList<GenreList> = ArrayList()
            map.keys.forEach { key ->
                genreList.add(GenreList(key, map[key]!!.toList()))
            }
            genreListsResource.postValue(Resource.Success(genreList))
        }
    }

    fun fetchGenreList(genre: String) {
        genreListResource.postValue(Resource.Loading())
        viewModelScope.launch {
            val booksByGenre = booksRepository.getBooksByGenre(genre)
            Log.i("dev", "Viewmodel $booksByGenre")
            genreListResource.postValue(Resource.Success(booksByGenre))
        }
    }

    fun fetchYouWillLike() {
        viewModelScope.launch {
            val youWillLYikes = booksRepository.getYouWillAlsoLike()
            val books = ArrayList<Book>()
            youWillLYikes.forEach {
                books.add(booksRepository.getBookById(it.bookId))
            }
            youWillLikeResource.postValue(Resource.Success(books))
        }
    }

    fun fetchBookById(id: Long) {
        viewModelScope.launch {
            bookResource.postValue(Resource.Success(booksRepository.getBookById(id)))
        }
    }

    fun fetchTopBannerSlides() {
        viewModelScope.launch {
            topBannerSlideResource.postValue(Resource.Success(booksRepository.getTopBannerSlides()))
        }
    }

    private fun saveDataToDB(response: Response) {
        viewModelScope.launch {
            booksRepository.clearDB()
            response.books.forEach { booksRepository.insertBook(it) }
            response.top_banner_slides.forEach { booksRepository.insertTopBannerSlide(it) }
            response.you_will_like_section.forEach {
                booksRepository.insertYouWillLike(
                    YouWillLike(null, it)
                )
            }
        }

    }

}