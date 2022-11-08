package com.example.corona19vaccinemapservice.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.corona19vaccinemapservice.R
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



    }
}