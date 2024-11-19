package com.example.bitfitpart2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepDao {
    @Query("SELECT * FROM sleep_table ORDER BY date DESC")
    fun getAll(): Flow<List<SleepEntity>>

    @Insert
    fun insert(sleepEntry: SleepEntity): Long

    @Query("DELETE FROM sleep_table")
    fun deleteAll(): Int
}