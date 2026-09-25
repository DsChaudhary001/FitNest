package com.fitnest.app.data.repository

import com.fitnest.app.data.db.AppDatabase
import com.fitnest.app.data.models.*
import kotlinx.coroutines.flow.Flow

/**
 * Single repository fronting the local Room database.
 * Swap this out for a Firebase-backed implementation later without
 * touching the UI layer, as long as it exposes the same functions.
 */
class FitNestRepository(private val db: AppDatabase) {

    // Profile
    fun observeProfile(): Flow<UserProfile?> = db.userProfileDao().observeProfile()
    suspend fun getProfile(): UserProfile? = db.userProfileDao().getProfile()
    suspend fun saveProfile(profile: UserProfile) = db.userProfileDao().upsert(profile)

    // Food
    suspend fun logFood(entry: FoodEntry) = db.foodDao().insert(entry)
    fun observeFoodForRange(start: Long, end: Long): Flow<List<FoodEntry>> =
        db.foodDao().observeForRange(start, end)
    fun observeAllFood(): Flow<List<FoodEntry>> = db.foodDao().observeAll()

    // Water
    suspend fun logWater(entry: WaterEntry) = db.waterDao().insert(entry)
    fun observeWaterForRange(start: Long, end: Long): Flow<List<WaterEntry>> =
        db.waterDao().observeForRange(start, end)

    // Workouts
    suspend fun logWorkout(log: WorkoutLog) = db.workoutLogDao().insert(log)
    fun observeWorkoutLogs(): Flow<List<WorkoutLog>> = db.workoutLogDao().observeAll()

    // Weight / progress
    suspend fun logWeight(entry: WeightEntry) = db.weightDao().insert(entry)
    fun observeWeightHistory(): Flow<List<WeightEntry>> = db.weightDao().observeAll()
}

/** Hardcoded starter workout library (Phase 1 — no backend needed). */
object WorkoutLibrary {
    val all: List<Workout> = listOf(
        Workout(
            id = "full_body_beginner_10",
            title = "10-Minute Full Body Starter",
            category = "Full Body",
            level = "Beginner",
            equipment = "No Equipment",
            durationMinutes = 10,
            exercises = listOf(
                Exercise("Bodyweight Squats", "Feet shoulder-width, lower hips back and down, keep chest up.", "12 reps"),
                Exercise("Push-Ups (or knee push-ups)", "Hands under shoulders, lower chest to floor, push back up.", "10 reps"),
                Exercise("Glute Bridge", "Lie on back, knees bent, lift hips up squeezing glutes.", "15 reps"),
                Exercise("Plank", "Forearms on floor, body in a straight line, brace your core.", "30 sec"),
                Exercise("Standing March", "Alternate lifting knees to hip height at a steady pace.", "40 sec")
            )
        ),
        Workout(
            id = "core_quick_5",
            title = "5-Minute Core Blast",
            category = "Core",
            level = "Beginner",
            equipment = "No Equipment",
            durationMinutes = 5,
            exercises = listOf(
                Exercise("Crunches", "Lie on back, knees bent, curl shoulders toward hips.", "15 reps"),
                Exercise("Plank", "Hold a straight line from head to heels.", "30 sec"),
                Exercise("Bicycle Crunches", "Alternate elbow to opposite knee.", "20 reps"),
                Exercise("Leg Raises", "Lie flat, raise straight legs to 90 degrees, lower slowly.", "12 reps")
            )
        ),
        Workout(
            id = "upper_body_15",
            title = "Upper Body Strength",
            category = "Upper Body",
            level = "Beginner",
            equipment = "No Equipment",
            durationMinutes = 15,
            exercises = listOf(
                Exercise("Push-Ups", "Standard or knee variation, full range of motion.", "3 x 10 reps"),
                Exercise("Tricep Dips (chair)", "Hands on chair edge, lower and raise your body.", "3 x 10 reps"),
                Exercise("Pike Push-Ups", "Hips high, lower head toward floor between hands.", "3 x 8 reps"),
                Exercise("Arm Circles", "Small controlled circles, both directions.", "30 sec each way")
            )
        ),
        Workout(
            id = "lower_body_15",
            title = "Lower Body Burn",
            category = "Lower Body",
            level = "Intermediate",
            equipment = "No Equipment",
            durationMinutes = 15,
            exercises = listOf(
                Exercise("Squats", "Full depth, controlled tempo.", "3 x 15 reps"),
                Exercise("Lunges", "Alternate legs, front knee over ankle.", "3 x 10 reps each leg"),
                Exercise("Calf Raises", "Rise onto toes, lower slowly.", "3 x 20 reps"),
                Exercise("Wall Sit", "Back flat against wall, knees at 90 degrees.", "45 sec")
            )
        ),
        Workout(
            id = "mobility_stretch_10",
            title = "Full Body Mobility & Stretch",
            category = "Mobility",
            level = "Beginner",
            equipment = "No Equipment",
            durationMinutes = 10,
            exercises = listOf(
                Exercise("Cat-Cow Stretch", "On hands and knees, alternate arching and rounding spine.", "10 reps"),
                Exercise("World's Greatest Stretch", "Lunge with a rotation, alternate sides.", "6 reps each side"),
                Exercise("Hip Circles", "Slow controlled circles, both directions.", "30 sec each way"),
                Exercise("Standing Forward Fold", "Hinge at hips, let arms hang, relax.", "45 sec")
            )
        )
    )
}
