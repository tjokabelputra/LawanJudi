package com.dicoding.lawanjudi.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.lawanjudi.database.ChatRepository
import com.dicoding.lawanjudi.di.Injection
import com.dicoding.lawanjudi.ui.gemini.GeminiViewModel

class ChatModelFactory private constructor(private val chatRepository: ChatRepository) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GeminiViewModel::class.java)){
            return GeminiViewModel(chatRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object{
        @Volatile
        private var instance: ChatModelFactory? = null
        fun getInstance(context: Context): ChatModelFactory =
            instance ?: synchronized(this){
                instance ?: ChatModelFactory(Injection.provideChatRepository(context))
            }.also { instance = it }
    }
}