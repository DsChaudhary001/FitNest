package com.fitnest.app.ui.workout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fitnest.app.data.models.Workout
import com.fitnest.app.data.models.WorkoutLog
import com.fitnest.app.data.repository.WorkoutLibrary

@Composable
fun WorkoutScreen(onComplete: (WorkoutLog) -> Unit) {
    var selected by remember { mutableStateOf<Workout?>(null) }

    if (selected == null) {
        WorkoutList(onSelect = { selected = it })
    } else {
        WorkoutDetail(
            workout = selected!!,
            onBack = { selected = null },
            onFinish = { completed ->
                onComplete(
                    WorkoutLog(
                        workoutName = selected!!.title,
                        category = selected!!.category,
                        durationMinutes = selected!!.durationMinutes,
                        completed = completed,
                        timestampMillis = System.currentTimeMillis()
                    )
                )
                selected = null
            }
        )
    }
}

@Composable
private fun WorkoutList(onSelect: (Workout) -> Unit) {
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Home Workouts", style = MaterialTheme.typography.headlineSmall)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(WorkoutLibrary.all) { workout ->
                ElevatedCard(onClick = { onSelect(workout) }, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text(workout.title, style = MaterialTheme.typography.titleMedium)
                        Text("${workout.category} · ${workout.level} · ${workout.equipment} · ${workout.durationMinutes} min")
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkoutDetail(workout: Workout, onBack: () -> Unit, onFinish: (Boolean) -> Unit) {
    var currentIndex by remember { mutableStateOf(0) }
    val exercise = workout.exercises.getOrNull(currentIndex)

    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        TextButton(onClick = onBack) { Text("← Back") }
        Text(workout.title, style = MaterialTheme.typography.headlineSmall)

        if (exercise != null) {
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Exercise ${currentIndex + 1} of ${workout.exercises.size}", style = MaterialTheme.typography.labelMedium)
                    Text(exercise.name, style = MaterialTheme.typography.titleLarge)
                    Text(exercise.instructions)
                    Text(exercise.repsOrDuration, style = MaterialTheme.typography.titleMedium)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (currentIndex < workout.exercises.size - 1) {
                    Button(onClick = { currentIndex++ }) { Text("Next exercise") }
                } else {
                    Button(onClick = { onFinish(true) }) { Text("Finish workout") }
                }
                OutlinedButton(onClick = { onFinish(false) }) { Text("Stop early") }
            }
        }
    }
}
