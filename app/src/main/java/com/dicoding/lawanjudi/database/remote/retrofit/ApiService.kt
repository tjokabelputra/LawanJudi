package com.dicoding.lawanjudi.database.remote.retrofit

import com.dicoding.lawanjudi.database.remote.response.AdsPredictReponse
import com.dicoding.lawanjudi.database.remote.response.AdsRequest
import com.dicoding.lawanjudi.database.remote.response.ContentRequest
import com.dicoding.lawanjudi.database.remote.response.ContentResponse
import com.dicoding.lawanjudi.database.remote.response.WebPredictResponse
import com.dicoding.lawanjudi.database.remote.response.WebRequest
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

    @POST("/web")
    suspend fun getWebResult(
        @Body request: WebRequest
    ) : Response<WebPredictResponse>

    @POST("/iklan")
    suspend fun getAdsResult(
        @Body request: AdsRequest
    ) : Response<AdsPredictReponse>
}