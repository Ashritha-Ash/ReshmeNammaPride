package com.example.reshmenammapride

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RecommendationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RecommendationRepository
    private val climateDao: ClimateDao
    private val engine: RecommendationEngine

    val allRecommendations: Flow<List<Recommendation>>
    private val _latestRecommendation = MutableStateFlow<Recommendation?>(null)
    val latestRecommendation: StateFlow<Recommendation?> = _latestRecommendation

    init {
        val db = AppDatabase.getDatabase(application)
        repository = RecommendationRepository(db.recommendationDao())
        climateDao = db.climateDao()
        engine = RecommendationEngine()
        allRecommendations = repository.allRecommendations
    }

    fun fetchLatestRecommendation(email: String) {
        viewModelScope.launch {
            repository.getLatestRecommendation(email).collectLatest {
                _latestRecommendation.value = it
            }
        }
    }

    fun generateRecommendation(email: String, currentBatchName: String) {
        viewModelScope.launch {
            climateDao.getEntriesForUser(email).collectLatest { entries ->
                if (entries.isNotEmpty()) {
                    val recommendation = engine.analyze(email, currentBatchName, entries)
                    repository.insert(recommendation)
                }
            }
        }
    }
}
