package com.example.reshmenammapride

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecommendationDao {
    @Insert
    suspend fun insertRecommendation(recommendation: Recommendation)

    @Query("SELECT * FROM recommendations ORDER BY timestamp DESC")
    fun getAllRecommendations(): Flow<List<Recommendation>>

    @Query("SELECT * FROM recommendations WHERE userEmail = :email ORDER BY timestamp DESC LIMIT 1")
    fun getLatestRecommendation(email: String): Flow<Recommendation?>
}
