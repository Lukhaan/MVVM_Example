package com.example.mvvm_example.util
import com.android.volley.VolleyError

interface ServerResponse<Res> {
    fun onSuccess(result: Res)
    fun onFail(error: VolleyError)
}