package com.dicoding.lawanjudi.database.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "url")
    val imageUrl: String = "",

    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "content")
    val content: String = ""
)