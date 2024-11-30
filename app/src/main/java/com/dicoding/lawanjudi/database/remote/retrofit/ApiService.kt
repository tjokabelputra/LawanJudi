package com.dicoding.lawanjudi.database.remote.retrofit

import com.dicoding.lawanjudi.database.remote.response.ContentRequest
import com.dicoding.lawanjudi.database.remote.response.ContentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("v1beta/models/gemini-1.5-flash:generateContent")
    suspend fun getAIResponse(
        @Query("key") apiKey: String,
        @Body request: ContentRequest
    ) : Response<ContentResponse>
}