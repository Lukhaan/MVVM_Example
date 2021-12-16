package com.example.mvvm_example

import android.app.Application
import android.content.Context
import com.example.mvvm_example.model.CommentRoomDatabase
import com.example.mvvm_example.model.PostRoomDatabase

class MainApplication : Application() {


    override fun onCreate() {
        appContext = applicationContext
        super.onCreate()
    }

    companion object {
        lateinit var appContext: Context
            private set
        val postDatabase: PostRoomDatabase by lazy { PostRoomDatabase.getDatabase(appContext)}
        val commentDatabase: CommentRoomDatabase by lazy { CommentRoomDatabase.getDatabase(appContext)}
    }
}