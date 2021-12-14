package com.example.mvvm_example

import android.app.Application
import android.content.Context

class MainApplication : Application() {
    override fun onCreate() {
        appContext = applicationContext
        super.onCreate()
    }

    companion object {
        lateinit var appContext: Context
            private set
    }
}