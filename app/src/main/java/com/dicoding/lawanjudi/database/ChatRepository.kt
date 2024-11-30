package com.dicoding.lawanjudi.database

import com.dicoding.lawanjudi.BuildConfig
import com.dicoding.lawanjudi.database.local.entity.ChatEntity
import com.dicoding.lawanjudi.database.local.room.ChatDao
import com.dicoding.lawanjudi.database.remote.response.ContentRequest
import com.dicoding.lawanjudi.database.remote.retrofit.ApiService

class ChatRepository private constructor(
    private val apiService: ApiService,
    private val chatDao: ChatDao
){
    private val apiKey = BuildConfig.GEMINI_KEY

    suspend fun callAI(request: ContentRequest) = apiService.getAIResponse(apiKey, request)

    fun getChats() = chatDao.getChats()

    suspend fun addChat(chat: ChatEntity) = chatDao.insertChat(chat)

    suspend fun deleteChat() = chatDao.clearChats()

    companion object{
        @Volatile
        private var instance: ChatRepository? = null
        fun getInstance(
            apiService: ApiService,
            chatDao: ChatDao
        ) : ChatRepository =
            instance ?: synchronized(this){
                instance ?: ChatRepository(
                    apiService,
                    chatDao
                )
            }.also { instance = it }
    }
}