package com.example.mvvm_example.util

import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mvvm_example.MainApplication
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.lang.reflect.Type

class ServerManager {
    companion object {
        val instance = ServerManager()
    }

    private val volleyQueue: RequestQueue by lazy {
        Volley.newRequestQueue(
            MainApplication.appContext
        )
    }

    fun <Res> request(endpoint: Endpoint, body: Map<String, String>? = null, out: Type, overloads: Map<String, String> = HashMap(), callback: VolleyCallback<Res>) {
        var urlWithParams = endpoint.url
        if (overloads.isNotEmpty()) {
            overloads.forEach { param ->
                urlWithParams = urlWithParams.replace(
                    "{%s}".format(param.key), param.value
                )
            }
        }

        val req: StringRequest = object : StringRequest(endpoint.method, urlWithParams,
            { response ->
                try {
                    val deserializedResponse = Gson().fromJson<Res>(response, out)
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

