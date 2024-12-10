package com.dicoding.lawanjudi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.lawanjudi.database.AiRepository
import com.dicoding.lawanjudi.di.Injection
import com.dicoding.lawanjudi.ui.add.AddViewModel

class AiModelFactory private constructor(private val aiRepository: AiRepository) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddViewModel::class.java)) {
            return AddViewModel(aiRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object{
        @Volatile
        private var instance: AiModelFactory? = null
        fun getInstance(): AiModelFactory =
            instance ?: synchronized(this) {
                instance ?: AiModelFactory(Injection.provideAiRepository())
            }.also { instance = it }
    }
}