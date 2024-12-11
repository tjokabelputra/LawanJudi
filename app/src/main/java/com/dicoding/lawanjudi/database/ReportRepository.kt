package com.dicoding.lawanjudi.database

import com.dicoding.lawanjudi.database.local.entity.ReportEntity
import com.dicoding.lawanjudi.database.local.room.ReportDao

class ReportRepository private constructor(
    private val reportDao: ReportDao
){
    fun getReports() = reportDao.getReports()

    fun getReportDetail(id: String) = reportDao.getReportById(id)

    suspend fun addReport(reports: List<ReportEntity>) = reportDao.insertReport(reports)

    suspend fun deleteReports() = reportDao.clearReports()

    companion object{
        @Volatile
        private var instance: ReportRepository? = null
        fun getInstance(
            reportDao: ReportDao
        ) : ReportRepository =
            instance ?: synchronized(this){
                instance ?: ReportRepository(
                    reportDao
                )
            }.also { instance = it }
    }
}