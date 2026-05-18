package com.example.reshmenammapride

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val email: String,
    val name: String,
    val password: String,
    val phone: String = "",
    val village: String = "",
    val experience: String = "Beginner" // Beginner, Intermediate, Expert
)