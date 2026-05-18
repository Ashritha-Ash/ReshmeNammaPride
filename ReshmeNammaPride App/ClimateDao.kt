package com.example.reshmenammapride

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

import kotlinx.coroutines.flow.Flow

@Dao
interface ClimateDao {

    @Insert
    suspend fun insertEntry(entry: ClimateEntry): Long

    @Query("SELECT * FROM climate_entries WHERE userEmail = :email ORDER BY timestamp DESC")
    fun getEntriesForUser(email: String): Flow<List<ClimateEntry>>

    @Query("SELECT * FROM climate_entries WHERE userEmail = :email ORDER BY timestamp DESC LIMIT 10")
    suspend fun getRecentEntries(email: String): List<ClimateEntry>
}