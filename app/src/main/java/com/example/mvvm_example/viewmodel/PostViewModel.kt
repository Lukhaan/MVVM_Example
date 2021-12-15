package com.example.mvvm_example.viewmodel

class PostViewModel(
    val id: Int,
    val title: String,
    val content: String,
    var comments: List<CommentViewModel>
)

class CommentViewModel(
    val id: Int,
    val title: String,
    val email: String,
    val content: String,
)