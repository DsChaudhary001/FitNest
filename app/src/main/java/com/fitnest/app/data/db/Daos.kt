package com.fitnest.app.data.db

import androidx.room.*
import com.fitnest.app.data.models.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun observeProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getProfile(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(profile: UserProfile)
}

@Dao
interface FoodDao {
    @Insert
    suspend fun insert(entry: FoodEntry)

    @Query("SELECT * FROM food_entries WHERE timestampMillis BETWEEN :start AND :end ORDER BY timestampMillis DESC")
    fun observeForRange(start: Long, end: Long): Flow<List<FoodEntry>>

    @Query("SELECT * FROM food_entries ORDER BY timestampMillis DESC")
    fun observeAll(): Flow<List<FoodEntry>>
}

@Dao
interface WaterDao {
    @Insert
    suspend fun insert(entry: WaterEntry)

    @Query("SELECT * FROM water_entries WHERE timestampMillis BETWEEN :start AND :end ORDER BY timestampMillis DESC")
    fun observeForRange(start: Long, end: Long): Flow<List<WaterEntry>>
}

@Dao
interface WorkoutLogDao {
    @Insert
    suspend fun insert(log: WorkoutLog)

    @Query("SELECT * FROM workout_logs ORDER BY timestampMillis DESC")
    fun observeAll(): Flow<List<WorkoutLog>>
}

@Dao
interface WeightDao {
    @Insert
    suspend fun insert(entry: WeightEntry)

    @Query("SELECT * FROM weight_history ORDER BY timestampMillis DESC")
    fun observeAll(): Flow<List<WeightEntry>>
}
