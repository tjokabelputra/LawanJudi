package com.dicoding.lawanjudi.util

object StringFormatter {
    fun percentFormatter(number: Double): String {
        val percent = number * 100
        return String.format("%.2f%%", percent)
    }
}