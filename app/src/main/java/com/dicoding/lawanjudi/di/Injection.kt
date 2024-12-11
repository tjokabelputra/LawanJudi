package com.dicoding.lawanjudi.di

import android.content.Context
import com.dicoding.lawanjudi.database.AiRepository
import com.dicoding.lawanjudi.database.ChatRepository
import com.dicoding.lawanjudi.database.ReportRepository
import com.dicoding.lawanjudi.database.local.room.ChatDatabase
import com.dicoding.lawanjudi.database.local.room.ReportDatabase
import com.dicoding.lawanjudi.database.remote.retrofit.ApiConfig

object Injection {
    fun provideChatRepository(context: Context): ChatRepository {
        val apiService = ApiConfig.getGeminiService()
        val database = ChatDatabase.getInstance(context)
        val dao = database.ChatDao()
        return ChatRepository.getInstance(apiService, dao)
    }

    fun provideAiRepository(): AiRepository {
        val apiService = ApiConfig.getAiService()
        return AiRepository.getInstance(apiService)
    }

    fun provideReportRepository(context: Context): ReportRepository {
        val database = ReportDatabase.getInstance(context)
        val dao = database.reportDao()
        return ReportRepository.getInstance(dao)
    }
}