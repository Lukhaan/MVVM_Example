package com.example.mvvm_example.util

import com.android.volley.Request

enum class Endpoint(val method: Int, val url: String) {
    GetPosts(Request.Method.GET, "https://jsonplaceholder.typicode.com/posts"),
    GetComments(Request.Method.GET, "https://jsonplaceholder.typicode.com/posts/{postId}/comments"),
}