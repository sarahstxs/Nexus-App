package com.example.nexusappxml.data.network

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.nexusappxml.BuildConfig
import java.util.concurrent.TimeUnit

object RetrofitClient {
    fun getInstance(context: Context): ApiService {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Aumenta o tempo para conectar
            .readTimeout(30, TimeUnit.SECONDS)    // Aumenta o tempo para ler a resposta do FastAPI
            .writeTimeout(30, TimeUnit.SECONDS)   // Aumenta o tempo para enviar os dados (como o deck)
            .addInterceptor(AuthInterceptor(context))
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}