package com.books.app.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.WindowInsets
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import androidx.annotation.RequiresApi
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.books.app.R
import kotlin.math.abs


fun hideStatusBar(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        activity.window.insetsController?.hide(WindowInsets.Type.statusBars())
    } else {
        activity.window.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN)
    }
}

fun Exception.getMessageFromException(context: Context) =
    localizedMessage ?: context.getString(R.string.something_goes_wrong)

fun <T> List<T>.prepareForTwoWayPaging(): List<T> {
    val first = first()
    val last = last()
    return toMutableList().apply {
        add(0, last)
        add(first)
    }
}

fun getCompositePageTransformer(): CompositePageTransformer {
    val compositePageTransformer = CompositePageTransformer()
    compositePageTransformer.apply {
        addTransformer(MarginPageTransformer(40))
        addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85F + r * 0.15F
        }
    }
    return compositePageTransformer
}


fun hasInternetConnection(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =

            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

    if (capabilities != null) {
        when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
        }
    }
    return false
}