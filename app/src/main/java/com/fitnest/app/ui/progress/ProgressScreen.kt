package com.fitnest.app.ui.progress

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.fitnest.app.data.models.FoodEntry
import com.fitnest.app.data.models.WeightEntry
import com.fitnest.app.data.models.WorkoutLog

@Composable
fun ProgressScreen(
    weightHistory: List<WeightEntry>,
    workoutLogs: List<WorkoutLog>,
    allFood: List<FoodEntry>,
    onLogWeight: (Float) -> Unit
) {
    var weightInput by remember { mutableStateOf("") }
    val loggingDays = allFood.map { it.timestampMillis / 86_400_000 }.distinct().size
    val completedWorkouts = workoutLogs.count { it.completed }

    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Progress", style = MaterialTheme.typography.headlineSmall)

        StatRow("Workouts completed", "$completedWorkouts")
        StatRow("Days with logged food", "$loggingDays")
        StatRow(
            "Current weight",
            weightHistory.firstOrNull()?.let { "${it.weightKg} kg" } ?: "Not logged yet"
        )

        Divider()
        Text("Log today's weight", style = MaterialTheme.typography.titleMedium)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = weightInput,
                onValueChange = { weightInput = it.filter { c -> c.isDigit() || c == '.' } },
                label = { Text("kg") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                weightInput.toFloatOrNull()?.let {
                    onLogWeight(it)
                    weightInput = ""
                }
            }) { Text("Save") }
        }

        Text(
            "Progress is here to help you understand your habits — not to chase extreme goals.",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label)
            Text(value, style = MaterialTheme.typography.titleMedium)
        }
    }
}
