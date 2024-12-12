package com.dicoding.lawanjudi.ui.education

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.lawanjudi.database.ArticleRepository
import com.dicoding.lawanjudi.database.local.entity.ArticleEntity
import kotlinx.coroutines.launch

class ArticleViewModel(private val articleRepository: ArticleRepository) : ViewModel() {

    fun getArticles() = articleRepository.getArticles()

    fun articleDetail(title: String) = articleRepository.getArticleDetail(title)

    fun saveArticles(articles: List<ArticleEntity>) = viewModelScope.launch {
        articleRepository.insertArticle(articles)
    }
}