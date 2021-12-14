package com.example.mvvm_example.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.VolleyError
import com.example.mvvm_example.model.Post
import com.example.mvvm_example.model.Responses
import com.example.mvvm_example.util.Endpoint
import com.example.mvvm_example.util.ServerManager
import com.example.mvvm_example.util.VolleyCallback

class PostsActivityViewModel(application: Application) : AndroidViewModel(application) {
    val posts = MutableLiveData<List<PostViewModel>>()
    val error = MutableLiveData<String>()

    fun getPosts() {
        ServerManager.instance.request(
            Endpoint.GetPosts,
            null,
            object : VolleyCallback<List<Post>> {
                override fun onSuccess(result: List<Post>) {
                    var p: List<PostViewModel> = result.map {
                        PostViewModel(it.title, it.body, ArrayList())
                    }
                    posts.postValue(p);
                }

                override fun onFail(err: VolleyError) {
                    error.postValue(err.message)
                    Log.e("Server Request Failed", err.message!!)
                }

            }
        )
    }

    fun getComments(postId: Int) {
//        ServerManager.instance.request(Endpoint.GetPosts, null)
    }
}

