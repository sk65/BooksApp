package com.books.app.di

import android.content.Context
import androidx.room.Room
import com.books.app.R
import com.books.app.database.BooksDatabase
import com.books.app.util.MINIMUM_FETCH_INTERVAL
import com.books.app.view.fragment.adapter.ChildAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_no_img)
            .error(R.drawable.ic_no_img)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
    )

    @Singleton
    @Provides
    fun provideGson(): Gson = Gson()

    @Singleton
    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = MINIMUM_FETCH_INTERVAL
        }
        remoteConfig.apply {
            setConfigSettingsAsync(configSettings)
        }
        return remoteConfig
    }

    @Singleton
    @Provides
    fun provideBookDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context, BooksDatabase::class.java,
        "book_db"
    ).build()

    @Singleton
    @Provides
    fun provideRedditDao(
        db: BooksDatabase
    ) = db.booksDao()


}