package com.fitnest.app.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fitnest.app.data.models.FoodEntry
import com.fitnest.app.data.models.UserProfile
import com.fitnest.app.data.models.WaterEntry
import com.fitnest.app.data.models.WorkoutLog

@Composable
fun HomeScreen(
    profile: UserProfile?,
    todayFood: List<FoodEntry>,
    todayWater: List<WaterEntry>,
    recentWorkouts: List<WorkoutLog>,
    onQuickWater: (Int) -> Unit,
    onGoToFood: () -> Unit,
    onGoToWorkout: () -> Unit
) {
    val caloriesToday = todayFood.sumOf { it.calories }
    val calorieGoal = profile?.dailyCalorieGoal ?: 2000
    val waterToday = todayWater.sumOf { it.amountMl }
    val waterGoal = profile?.dailyWaterGoalMl ?: 2000
    val workoutsCompletedToday = recentWorkouts.count { it.completed }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Hi ${profile?.name?.ifBlank { "there" } ?: "there"} 👋",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text("Here's your day so far", style = MaterialTheme.typography.bodyMedium)

        SummaryCard(
            title = "Nutrition",
            value = "$caloriesToday / $calorieGoal kcal",
            progress = (caloriesToday.toFloat() / calorieGoal.coerceAtLeast(1)).coerceIn(0f, 1f)
        )
        SummaryCard(
            title = "Water",
            value = "$waterToday / $waterGoal ml",
            progress = (waterToday.toFloat() / waterGoal.coerceAtLeast(1)).coerceIn(0f, 1f)
        )
        SummaryCard(
            title = "Workouts completed today",
            value = "$workoutsCompletedToday",
            progress = (workoutsCompletedToday / 3f).coerceIn(0f, 1f)
        )

        Text("Quick actions", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AssistChipRow(onQuickWater)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onGoToFood) { Text("Log food") }
            OutlinedButton(onClick = onGoToWorkout) { Text("Start workout") }
        }
    }
}

@Composable
private fun AssistChipRow(onQuickWater: (Int) -> Unit) {
    listOf(250, 500).forEach { ml ->
        AssistChip(onClick = { onQuickWater(ml) }, label = { Text("+${ml}ml water") })
        Spacer(Modifier.width(8.dp))
    }
}

@Composable
private fun SummaryCard(title: String, value: String, progress: Float) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall)
            Text(value, style = MaterialTheme.typography.headlineSmall)
            LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth())
        }
    }
}
