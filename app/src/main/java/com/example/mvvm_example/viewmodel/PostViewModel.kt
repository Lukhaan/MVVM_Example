package com.example.mvvm_example.viewmodel

data class PostViewModel(
    val id: Int,
    val title: String,
    val content: String,
    var comments: List<CommentViewModel>
)

