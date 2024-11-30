package com.dicoding.lawanjudi.ui.gemini

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.lawanjudi.database.ChatRepository
import com.dicoding.lawanjudi.database.Result
import com.dicoding.lawanjudi.database.local.entity.ChatEntity
import com.dicoding.lawanjudi.database.remote.response.ContentRequest
import com.dicoding.lawanjudi.database.remote.response.ContentResponse
import com.dicoding.lawanjudi.database.remote.response.ErrorResponse
import com.dicoding.lawanjudi.database.remote.response.Message
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class GeminiViewModel(private val chatRepository: ChatRepository) : ViewModel() {
    val promptResult = MutableLiveData<Result<ContentResponse>>()

    val chats: LiveData<List<ChatEntity>> = chatRepository.getChats()

    fun callApi(request: ContentRequest) = viewModelScope.launch {
        promptResult.postValue(Result.Loading)
        try {
            val response = chatRepository.callAI(request)
            if (response.isSuccessful) {
                val contentResponse = response.body()
                if(contentResponse != null){
                    promptResult.postValue(Result.Success(contentResponse))
                }
                else{
                    promptResult.postValue(Result.Error("Empty response body"))
                }
            }
            else{
                promptResult.postValue(Result.Error("Unsuccessful response"))
            }
        }
        catch (e: HttpException){
            val jsonString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonString, ErrorResponse::class.java)
            promptResult.postValue(Result.Error(errorBody.error.toString()))
        }
    }

    fun addChat(chat: ChatEntity) = viewModelScope.launch {
        chatRepository.addChat(chat)
    }

    fun deleteChat() = viewModelScope.launch {
        chatRepository.deleteChat()
    }
}