package com.example.mvvm_example.util

import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mvvm_example.MainApplication
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class ServerManager {
    companion object {
        val instance = ServerManager()
    }

    val volleyQueue: RequestQueue by lazy {
        Volley.newRequestQueue(
            MainApplication.appContext
        )
    }

    inline fun <reified Res> request(endpoint: Endpoint, body: Map<String, String>? = null, callback: VolleyCallback<Res>) {
        val req: StringRequest = object : StringRequest(endpoint.method, endpoint.url,
            { response ->
                try {
                    val deserializedResponse = Gson().fromJson(response, Res::class.java)
                    callback.onSuccess(deserializedResponse)
                } catch (ex: JsonSyntaxException) {
                    callback.onFail(VolleyError("Couldn't deserialize response"))
                } catch (ex: Exception) {
                    callback.onFail(VolleyError("Unknown exception: $ex"))
                }
            },
            { error ->
                callback.onFail(error)
            }) {
            override fun getParams(): Map<String, String>? {
                return body
            }
        }

        volleyQueue.add(req)
    }
}

