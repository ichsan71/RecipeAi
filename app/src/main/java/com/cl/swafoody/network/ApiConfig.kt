package com.cl.swafoody.network

import android.util.Log
import com.cl.swafoody.data.source.remote.response.LoginResponse
import com.cl.swafoody.utils.SharedPrefs
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.orhanobut.hawk.Hawk
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }

        fun getBackendApiService(): ApiService {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val headerInterceptor = Interceptor { chain ->
                val request = chain.request()
                val loginData : LoginResponse? = Hawk.get(SharedPrefs.KEY_LOGIN)

                val apiToken =  "Bearer " + loginData?.token
                val newRequest = request.newBuilder()
                    .addHeader("Authorization", apiToken ?: "")
                    .build()

                Log.d("Authorization", apiToken ?: "")

                chain.proceed(newRequest)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(headerInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://swafoody.fajarbuana.my.id")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}