package com.dicoding.lawanjudi.database.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.lawanjudi.database.local.entity.ArticleEntity

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles")
    fun getArticles() : LiveData<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE title = :title")
    fun getArticleDetail(title: String) : LiveData<ArticleEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Query("SELECT COUNT(*) FROM articles")
    suspend fun getArticleCount(): Int

}