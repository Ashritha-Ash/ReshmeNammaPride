package com.example.reshmenammapride

import kotlinx.coroutines.flow.Flow

class RecommendationRepository(private val recommendationDao: RecommendationDao) {
    val allRecommendations: Flow<List<Recommendation>> = recommendationDao.getAllRecommendations()
    
    fun getLatestRecommendation(email: String): Flow<Recommendation?> = 
        recommendationDao.getLatestRecommendation(email)

    suspend fun insert(recommendation: Recommendation) {
        recommendationDao.insertRecommendation(recommendation)
    }
}
