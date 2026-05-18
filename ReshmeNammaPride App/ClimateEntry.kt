package com.example.reshmenammapride

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "climate_entries")
data class ClimateEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userEmail: String,
    val breed: String,
    val day: Int,
    val stage: String,
    val temperature: Double,
    val humidity: Double,
    val status: String,
    val advice: String,
    val timestamp: Long, // Store as System.currentTimeMillis()
    val weatherCondition: String, // Sunny, Rainy, Cloudy, etc.
    val season: String // Summer, Monsoon, Winter
)