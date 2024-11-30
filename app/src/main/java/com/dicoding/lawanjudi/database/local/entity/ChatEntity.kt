package com.dicoding.lawanjudi.database.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat")
data class ChatEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @field:ColumnInfo(name = "role")
    val role: String,

    @field:ColumnInfo(name = "message")
    val message: String
)
