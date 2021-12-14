package com.example.mvvm_example.util
import com.android.volley.VolleyError

interface VolleyCallback<T> {
    fun onSuccess(result: T)
    fun onFail(error: VolleyError)
}