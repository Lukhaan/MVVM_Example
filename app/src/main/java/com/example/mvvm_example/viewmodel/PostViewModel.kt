package com.example.mvvm_example.viewmodel

class PostViewModel(
    val title: String,
    val content: String,
    val comments: List<CommentViewModel>
)

class CommentViewModel(
    val title: String,
    val email: String,
    val content: String,
)