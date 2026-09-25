package com.fitnest.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fitnest.app.data.models.*

@Database(
    entities = [UserProfile::class, FoodEntry::class, WaterEntry::class, WorkoutLog::class, WeightEntry::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun foodDao(): FoodDao
    abstract fun waterDao(): WaterDao
    abstract fun workoutLogDao(): WorkoutLogDao
    abstract fun weightDao(): WeightDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitnest.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
