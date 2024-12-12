package com.dicoding.lawanjudi.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.lawanjudi.database.ArticleRepository
import com.dicoding.lawanjudi.di.Injection
import com.dicoding.lawanjudi.ui.education.ArticleViewModel

class ArticleModelFactory private constructor(private val articleRepository: ArticleRepository) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleViewModel::class.java)){
            return ArticleViewModel(articleRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }


    companion object{
        @Volatile
        private var instance: ArticleModelFactory? = null
        fun getInstance(context: Context): ArticleModelFactory =
            instance ?: synchronized(this){
                instance ?: ArticleModelFactory(Injection.provideArticleRepository(context))
            }.also { instance = it }
    }
}