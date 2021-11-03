package com.books.app.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.books.app.R
import com.books.app.model.response.Response
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
class LoadingViewModel @Inject
constructor(
    @ApplicationContext val context: Context,
    private val remoteConfig: FirebaseRemoteConfig,
    private val booksRepository: BooksRepository,
    private val gson: Gson
) : ViewModel() {

    private var _resource = MutableLiveData<Resource<Unit>>()
    val resource: LiveData<Resource<Unit>> = _resource


    init {
        fetchData()
    }

    private fun fetchData() {
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
        _resource.postValue(Resource.Success(Unit))
    }
}