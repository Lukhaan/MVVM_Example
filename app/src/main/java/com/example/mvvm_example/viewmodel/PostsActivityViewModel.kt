package com.example.mvvm_example.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.VolleyError
import com.example.mvvm_example.model.Comment
import com.example.mvvm_example.model.Post
import com.example.mvvm_example.util.Endpoint
import com.example.mvvm_example.util.ServerManager
import com.example.mvvm_example.util.VolleyCallback
import java.util.ArrayList

sealed class PostEvent {
    object GetPosts: PostEvent()
    data class GetComments(val postId: Int): PostEvent()
}

class PostsActivityViewModel(application: Application) : AndroidViewModel(application) {
    val posts = MutableLiveData<List<PostViewModel>>()
    val detailedPost = MutableLiveData<PostViewModel>()
    val errorMessage = MutableLiveData<String>()

    fun handleEvent(event: PostEvent) {
        when(event) {
            is PostEvent.GetPosts -> getPosts()
            is PostEvent.GetComments -> getComments(event.postId)
        }
    }

    private fun getPosts() {
        ServerManager.instance.request(
            Endpoint.GetPosts,
            null,
            Array<Post>::class.java,
            callback = object : VolleyCallback<Array<Post>> {
                override fun onSuccess(result: Array<Post>) {
                    posts.postValue(result.map {
                        PostViewModel(it.id, it.title, it.body, ArrayList())
                    })
                }

                override fun onFail(error: VolleyError) {
                    errorMessage.postValue(error.message)
                    Log.e("Server Request Failed", error.message!!)
                }
            }
        )
    }

    private fun getComments(postId: Int) {
        ServerManager.instance.request(
            Endpoint.GetComments,
            null,
            Array<Comment>::class.java,
            overloads = hashMapOf("postId" to postId.toString()),
            object : VolleyCallback<Array<Comment>> {
                override fun onSuccess(result: Array<Comment>) {
                    val comments = result.map {
                        CommentViewModel(it.id, it.name, it.email, it.body)
                    }

                    posts.value?.get(postId)?.comments = comments
                    detailedPost.postValue(posts.value?.get(postId))
                }

                override fun onFail(error: VolleyError) {
                    errorMessage.postValue(error.message)
                    Log.e("Server Request Failed", error.message!!)
                }
            })
    }
}

