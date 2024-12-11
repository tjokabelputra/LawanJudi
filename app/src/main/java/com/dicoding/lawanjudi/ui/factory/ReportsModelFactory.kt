package com.dicoding.lawanjudi.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.lawanjudi.database.ReportRepository
import com.dicoding.lawanjudi.di.Injection
import com.dicoding.lawanjudi.ui.gemini.GeminiViewModel
import com.dicoding.lawanjudi.ui.history.HistoryViewModel


class ReportsModelFactory private constructor(private val reportRepository: ReportRepository) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)){
            return HistoryViewModel(reportRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object{
        @Volatile
        private var instance: ReportsModelFactory? = null
        fun getInstance(context: Context): ReportsModelFactory =
            instance ?: synchronized(this){
                instance ?: ReportsModelFactory(Injection.provideReportRepository(context))
            }.also { instance = it }
    }
}