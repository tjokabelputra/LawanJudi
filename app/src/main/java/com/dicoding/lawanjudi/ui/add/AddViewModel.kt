package com.dicoding.lawanjudi.ui.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.lawanjudi.database.AiRepository
import com.dicoding.lawanjudi.database.remote.response.AdsPredictReponse
import com.dicoding.lawanjudi.database.remote.response.WebPredictResponse
import com.dicoding.lawanjudi.database.Result
import com.dicoding.lawanjudi.database.remote.response.AdsRequest
import com.dicoding.lawanjudi.database.remote.response.ErrorResponse
import com.dicoding.lawanjudi.database.remote.response.WebRequest
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AddViewModel(private val aiRepository: AiRepository) : ViewModel() {
    val analyzeWebResult = MutableLiveData<Result<WebPredictResponse>>()

    val analyzeAdsResult = MutableLiveData<Result<AdsPredictReponse>>()

    fun analyzeWeb(url: String) = viewModelScope.launch {
        analyzeWebResult.postValue(Result.Loading)
        try {
            val request = WebRequest(url)
            val response = aiRepository.analyzeWeb(request)
            when {
                response.isSuccessful -> {
                    val contentResponse = response.body()
                    if (contentResponse != null) {
                        analyzeWebResult.postValue(Result.Success(contentResponse))
                    } else {
                        analyzeWebResult.postValue(Result.Error("Empty response body"))
                    }
                }
                else -> {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    analyzeWebResult.postValue(Result.Error(errorBody))
                }
            }
        }
        catch (e: HttpException){
            val jsonString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonString, ErrorResponse::class.java)
            analyzeWebResult.postValue(Result.Error(errorBody.error.toString()))
        }
    }

    fun analyzeAds(text: String) = viewModelScope.launch {
        analyzeAdsResult.postValue(Result.Loading)
        try {
            val request = AdsRequest(text)
            val response = aiRepository.analyzeAds(request)
            when {
                response.isSuccessful -> {
                    val contentResponse = response.body()
                    if (contentResponse != null) {
                        analyzeAdsResult.postValue(Result.Success(contentResponse))
                    } else {
                        analyzeAdsResult.postValue(Result.Error("Empty response body"))
                    }
                }
                else -> {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    analyzeAdsResult.postValue(Result.Error(errorBody))
                }
            }
        } catch (e: HttpException) {
            val jsonString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonString, ErrorResponse::class.java)
            analyzeWebResult.postValue(Result.Error(errorBody.error.toString()))
        }
    }
}