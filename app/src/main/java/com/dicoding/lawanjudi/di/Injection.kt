package com.dicoding.lawanjudi.di

import android.content.Context
import com.dicoding.lawanjudi.database.ChatRepository
import com.dicoding.lawanjudi.database.local.room.ChatDatabase
import com.dicoding.lawanjudi.database.remote.retrofit.ApiConfig

object Injection {
    fun provideChatRepository(context: Context): ChatRepository {
        val apiService = ApiConfig.getAiService()
        val database = ChatDatabase.getInstance(context)
        val dao = database.ChatDao()
        return ChatRepository.getInstance(apiService, dao)
    }
}