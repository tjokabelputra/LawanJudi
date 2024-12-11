package com.dicoding.lawanjudi.database.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.lawanjudi.database.local.entity.ReportEntity

@Database(entities = [ReportEntity::class], version = 1, exportSchema = false)
abstract class ReportDatabase : RoomDatabase() {
    abstract fun reportDao(): ReportDao

    companion object{
        @Volatile
        private var instance: ReportDatabase? = null

        fun getInstance(context: Context): ReportDatabase =
            instance ?: synchronized(this){
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    ReportDatabase::class.java, "reports.db"
                ).build()
            }
    }
}