package com.dicoding.lawanjudi.database.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class ReportEntity (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "ai_confirmed")
    val aiConfirmed: Boolean? = false

)