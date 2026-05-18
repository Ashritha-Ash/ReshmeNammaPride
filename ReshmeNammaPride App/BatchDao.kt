package com.example.reshmenammapride

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BatchDao {
    @Insert
    suspend fun insertBatch(batch: Batch): Long

    @Query("SELECT * FROM batches WHERE userEmail = :email ORDER BY startDate DESC")
    fun getBatchesForUser(email: String): Flow<List<Batch>>

    @Query("SELECT * FROM batches WHERE id = :id")
    suspend fun getBatchById(id: Int): Batch?
}