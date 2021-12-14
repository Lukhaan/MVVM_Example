package com.example.mvvm_example.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.viewModels
import com.example.mvvm_example.R
import com.example.mvvm_example.viewmodel.PostsActivityViewModel

class PostsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: PostsActivityViewModel by viewModels()

        viewModel.posts.observe(this, { posts ->
            val mListView = findViewById<ListView>(R.id.post_list_view)
            mListView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, posts)
        })

        setContentView(R.layout.activity_main)

        viewModel.getPosts()
    }
}