package com.fitnest.app.ui.nav

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fitnest.app.data.db.AppDatabase
import com.fitnest.app.data.models.*
import com.fitnest.app.data.repository.FitNestRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * App-wide ViewModel. In a bigger app you'd split this per screen;
 * kept together here to keep the MVP easy to follow.
 */
class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = FitNestRepository(AppDatabase.getInstance(application))

    val profile = repo.observeProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allFood = repo.observeAllFood()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val workoutLogs = repo.observeWorkoutLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val weightHistory = repo.observeWeightHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveProfile(profile: UserProfile) {
        viewModelScope.launch { repo.saveProfile(profile) }
    }

    fun logFood(entry: FoodEntry) {
        viewModelScope.launch { repo.logFood(entry) }
    }

    fun logWater(amountMl: Int) {
        viewModelScope.launch {
            repo.logWater(WaterEntry(amountMl = amountMl, timestampMillis = System.currentTimeMillis()))
        }
    }

    fun waterForToday(onResult: (List<WaterEntry>) -> Unit) {
        val (start, end) = todayRangeMillis()
        viewModelScope.launch {
            repo.observeWaterForRange(start, end)
        }
        // Exposed as a Flow instead for screens; see WaterFlow below.
    }

    fun observeWaterToday() = run {
        val (start, end) = todayRangeMillis()
        repo.observeWaterForRange(start, end)
    }

    fun observeFoodToday() = run {
        val (start, end) = todayRangeMillis()
        repo.observeFoodForRange(start, end)
    }

    fun logWorkout(log: WorkoutLog) {
        viewModelScope.launch { repo.logWorkout(log) }
    }

    fun logWeight(weightKg: Float) {
        viewModelScope.launch {
            repo.logWeight(WeightEntry(weightKg = weightKg, timestampMillis = System.currentTimeMillis()))
        }
    }

    private fun todayRangeMillis(): Pair<Long, Long> {
        val dayMs = 24L * 60 * 60 * 1000
        val now = System.currentTimeMillis()
        val start = now - (now % dayMs)
        return start to (start + dayMs)
    }
}
