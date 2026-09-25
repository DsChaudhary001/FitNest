package com.fitnest.app.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "",
    val age: Int = 0,
    val heightCm: Int = 0,
    val weightKg: Float = 0f,
    val activityLevel: String = "Moderate",
    val goal: String = "Improve general fitness",
    val dailyWaterGoalMl: Int = 2000,
    val dailyCalorieGoal: Int = 2000,
    val profileComplete: Boolean = false
)

@Entity(tableName = "food_entries")
data class FoodEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val servingSize: String,
    val calories: Int,
    val proteinG: Int,
    val carbsG: Int,
    val fatG: Int,
    val mealType: String,
    val timestampMillis: Long
)

@Entity(tableName = "water_entries")
data class WaterEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amountMl: Int,
    val timestampMillis: Long
)

@Entity(tableName = "workout_logs")
data class WorkoutLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val workoutName: String,
    val category: String,
    val durationMinutes: Int,
    val completed: Boolean,
    val timestampMillis: Long
)

@Entity(tableName = "weight_history")
data class WeightEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val weightKg: Float,
    val timestampMillis: Long
)

data class Exercise(
    val name: String,
    val instructions: String,
    val repsOrDuration: String
)

data class Workout(
    val id: String,
    val title: String,
    val category: String,
    val level: String,
    val equipment: String,
    val durationMinutes: Int,
    val exercises: List<Exercise>
)
