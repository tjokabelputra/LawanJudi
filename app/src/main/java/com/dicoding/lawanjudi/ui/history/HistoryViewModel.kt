package com.dicoding.lawanjudi.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.lawanjudi.database.ReportRepository
import com.dicoding.lawanjudi.database.local.entity.ReportEntity
import kotlinx.coroutines.launch

class HistoryViewModel(private val reportRepository: ReportRepository) : ViewModel(){

    fun addReports(reports: List<ReportEntity>) = viewModelScope.launch {
        reportRepository.addReport(reports)
    }

    fun getReportId(id: String) = reportRepository.getReportDetail(id)

    fun getReports() = reportRepository.getReports()

    fun deleteReports() = viewModelScope.launch {
        reportRepository.deleteReports()
    }
}