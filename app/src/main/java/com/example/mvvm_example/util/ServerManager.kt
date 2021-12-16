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

    private fun buildUri(endpoint: Endpoint, overloads: Map<String, String>): String {
        overloads.forEach { param ->
            endpoint.url.replace(
                "{%s}".format(param.key), param.value
            )
        }
        return endpoint.url
    }

    fun <Res> request(endpoint: Endpoint, body: Map<String, String>? = null, out: Type, overloads: Map<String, String> = HashMap(), callback: ServerResponse<Res>) {
        val req: StringRequest = object : StringRequest(endpoint.method, buildUri(endpoint, overloads),
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

