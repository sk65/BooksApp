package com.books.app.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.books.app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //  val toolbar = findViewById<Toolbar>(R.id.toolbar1)
        //     setSupportActionBar(toolbar)
//
//        val booksRepository = BooksRepository()
//        val remoteConfig = booksRepository.remoteConfig
//        remoteConfig.fetchAndActivate().addOnCompleteListener(this) { task ->
//            if (task.isSuccessful) {
//                val updated = task.result
//                Log.d("dev", "Config params updated: $updated")
//                Toast.makeText(
//                    this, "Fetch and activate succeeded",
//                    Toast.LENGTH_SHORT
//                ).show()
//            } else {
//                Toast.makeText(
//                    this, "Fetch failed",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//        val asString = remoteConfig[JSON_DATA_KEY].asString()
//        val gson = Gson()
//
//        Toast.makeText(this, fromJson.books[0].author, Toast.LENGTH_SHORT).show()
    }

}