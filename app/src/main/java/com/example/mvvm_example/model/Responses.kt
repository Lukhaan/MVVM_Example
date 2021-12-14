package com.example.mvvm_example.util
import com.example.mvvm_example.model.Post

object Responses {
    val GetPostsResponse(
        val posts: ArrayList<Post>
    )
}