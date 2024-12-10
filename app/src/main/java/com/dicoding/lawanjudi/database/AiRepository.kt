package com.dicoding.lawanjudi.database

import com.dicoding.lawanjudi.database.remote.response.AdsRequest
import com.dicoding.lawanjudi.database.remote.response.WebRequest
import com.dicoding.lawanjudi.database.remote.retrofit.ApiService

class AiRepository private constructor(
    private val apiService: ApiService,
){

    suspend fun analyzeWeb(request: WebRequest) = apiService.getWebResult(request)

    suspend fun analyzeAds(request: AdsRequest) = apiService.getAdsResult(request)

    companion object {
        @Volatile
        private var instance: AiRepository? = null
        fun getInstance(
            apiService: ApiService
        ): AiRepository =
            instance ?: synchronized(this) {
                instance ?: AiRepository(apiService)
            }.also { instance = it }
    }
}