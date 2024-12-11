package com.dicoding.lawanjudi.database.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.lawanjudi.database.local.entity.ReportEntity

@Dao
interface ReportDao {
    @Query("SELECT * FROM reports")
    fun getReports(): LiveData<List<ReportEntity>>

    @Query("SELECT * FROM reports WHERE id = :id")
    fun getReportById(id: String): LiveData<ReportEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReport(reports: List<ReportEntity>)

    @Query("DELETE FROM reports")
    suspend fun clearReports()
}