package com.example.reshmenammapride

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "batches")
data class Batch(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userEmail: String,
    val name: String,
    val breed: String,
    val startDate: Long
)