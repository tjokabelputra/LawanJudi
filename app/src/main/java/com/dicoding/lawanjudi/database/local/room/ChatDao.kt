package com.dicoding.lawanjudi.database.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.lawanjudi.database.local.entity.ChatEntity
@Dao
interface ChatDao {
    @Query("SELECT * FROM chat")
    fun getChats(): LiveData<List<ChatEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertChat(chat: ChatEntity)

    @Query("DELETE FROM chat")
    suspend fun clearChats()
}