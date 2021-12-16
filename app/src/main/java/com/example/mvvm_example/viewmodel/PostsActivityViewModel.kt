package com.example.mvvm_example.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.VolleyError
import com.example.mvvm_example.MainApplication
import com.example.mvvm_example.model.Comment
import com.example.mvvm_example.model.Post
import com.example.mvvm_example.util.Endpoint
import com.example.mvvm_example.util.ServerManager
import com.example.mvvm_example.util.ServerResponse
import kotlinx.coroutines.*
import java.util.ArrayList
import kotlin.coroutines.CoroutineContext

sealed class PostEvent {
    object GetPosts: PostEvent()
    data class GetComments(val postId: Int): PostEvent()
}

class PostsActivityViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    override val coroutineContext: CoroutineContext get() = Dispatchers.Default

    val posts = MutableLiveData<List<PostViewModel>>()
    val detailedPost = MutableLiveData<PostViewModel>()
    val errorMessage = MutableLiveData<String>()

    fun handleEvent(event: PostEvent) {
        when(event) {
            is PostEvent.GetPosts -> getPosts()
            is PostEvent.GetComments -> getDetails(event.postId)
        }
    }

    private fun mapPostModelToViewModel(posts: Array<Post>): List<PostViewModel> {
        return posts.map {
            PostViewModel(it.id, it.title, it.body, ArrayList())
        }
    }

    private fun mapCommentModelToViewModel(comments: Array<Comment>): List<CommentViewModel> {
        return comments.map {
            CommentViewModel(it.id, it.email, it.body)
        }
    }

    private fun getPosts() = launch {
        //If we already have the data use that otherwise request from server
        val mPosts = MainApplication.postDatabase.dao().query()
        if (mPosts.isNotEmpty()) {
            posts.postValue(mapPostModelToViewModel(mPosts))
        } else {
            ServerManager.instance.request(
                Endpoint.GetPosts,
                null,
                Array<Post>::class.java,
                callback = object : ServerResponse<Array<Post>> {
                    override fun onSuccess(result: Array<Post>) {
                        //Dispatch database operation to async IO thread for better performance
                        CoroutineScope(Dispatchers.IO).launch {
                            result.forEach { MainApplication.postDatabase.dao().insert(it) }
                        }
                        posts.postValue(mapPostModelToViewModel(result))
                    }

                    override fun onFail(error: VolleyError) {
                        errorMessage.postValue(error.message)
                        Log.e("Server Request Failed", error.message!!)
                    }
                }
            )
        }
    }

    private fun getDetails(postId: Int) = launch {
        //If we already have the data use that otherwise request from server
        val mComments = MainApplication.commentDatabase.dao().query(postId)

        if(mComments.isNotEmpty()) {
            val comments = mapCommentModelToViewModel(mComments)
            addCommentsToPostViewModel(postId, comments)
        } else {
            ServerManager.instance.request(
                Endpoint.GetComments,
                null,
                Array<Comment>::class.java,
                overloads = hashMapOf("postId" to postId.toString()),
                object : ServerResponse<Array<Comment>> {
                    override fun onSuccess(result: Array<Comment>) {
                        //Dispatch database operation to async IO thread for better performance
                        CoroutineScope(Dispatchers.IO).launch {
                            result.forEach {
                                MainApplication.commentDatabase.dao().insert(it)
                            }
                        }
                        addCommentsToPostViewModel(postId, mapCommentModelToViewModel(result))
                    }

                    override fun onFail(error: VolleyError) {
                        errorMessage.postValue(error.message)
                        Log.e("Server Request Failed", error.message!!)
                    }
                }
            )
        }
    }

    private fun addCommentsToPostViewModel(postId: Int, comments: List<CommentViewModel>) {
        posts.value?.indexOfFirst { it.id == postId }?.let {
            posts.value?.get(it)?.comments = comments
            detailedPost.postValue(posts.value?.get(it))
        }
    }
}

