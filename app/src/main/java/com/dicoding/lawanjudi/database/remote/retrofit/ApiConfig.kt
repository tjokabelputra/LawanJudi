package com.dicoding.lawanjudi.database.remote.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.dicoding.lawanjudi.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private const val GEMINI_URL = BuildConfig.GEMINI_URL
    private const val AI_URL = BuildConfig.AI_URL

    fun getGeminiService(): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor()
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(GEMINI_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    fun getAiService(): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(AI_URL)
            .addConverterFactory((GsonConverterFactory.create()))
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}