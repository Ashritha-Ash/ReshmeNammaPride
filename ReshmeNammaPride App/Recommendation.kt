package com.example.reshmenammapride

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recommendations")
data class Recommendation(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userEmail: String,
    val batchName: String,
    val stage: String,
    val riskLevel: String, // Low, Medium, High
    val reason: String,
    val advice: String,
    val timestamp: Long
)
