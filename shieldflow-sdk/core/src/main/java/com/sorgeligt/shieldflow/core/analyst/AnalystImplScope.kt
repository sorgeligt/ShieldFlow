package com.sorgeligt.shieldflow.core.analyst

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

public interface AnalystImplScope {
    public val AnalystImplScope.analyst: Analyst get() = object : Analyst {
        override fun log(
            event: String,
            params: Map<String, Any?>?
        ) {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5050") // localhost с эмулятора
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(LogApi::class.java)
            val log = LogEvent(event, params)

            api.sendLog(log).enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    Log.d("Analyst", "Log sent: $event")
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.e("Analyst", "Failed to send log", t)
                }
            })
        }
    }
}
