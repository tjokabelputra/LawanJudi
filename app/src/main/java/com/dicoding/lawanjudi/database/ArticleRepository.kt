package com.dicoding.lawanjudi.database

import com.dicoding.lawanjudi.database.local.entity.ArticleEntity
import com.dicoding.lawanjudi.database.local.room.ArticleDao

class ArticleRepository private constructor(
    private val articleDao: ArticleDao
){
    fun getArticles() = articleDao.getArticles()

    fun getArticleDetail(title: String) = articleDao.getArticleDetail(title)

    suspend fun insertArticle(articles: List<ArticleEntity>) {
        if (articleDao.getArticleCount() == 0) {
            articleDao.insertArticles(articles)
        }
    }

    companion object{
        @Volatile
        private var instance: ArticleRepository? = null

        fun getInstance(
            articleDao: ArticleDao
        ) : ArticleRepository =
            instance ?: synchronized(this){
                instance ?: ArticleRepository(articleDao)
                }.also { instance = it }

    }
}