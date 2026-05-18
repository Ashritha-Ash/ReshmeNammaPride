package com.example.reshmenammapride

import androidx.room.Database
import androidx.room.RoomDatabase
import android.util.Log

@Database(entities = [ClimateEntry::class, User::class, Batch::class, Recommendation::class], version = 5)
abstract class AppDatabase : RoomDatabase() {
    abstract fun climateDao(): ClimateDao
    abstract fun userDao(): UserDao
    abstract fun batchDao(): BatchDao
    abstract fun recommendationDao(): RecommendationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: android.content.Context): AppDatabase {
            Log.d("ReshmeDebug", "AppDatabase.getDatabase called")
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "reshme_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}