package com.example.learnkotlin.Store

import android.app.Application
import com.example.learnkotlin.Components.FavoriteViewModel

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FavoriteViewModel.initialize(this)
    }
}