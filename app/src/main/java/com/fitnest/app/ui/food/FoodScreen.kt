package com.fitnest.app.ui.food

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.fitnest.app.data.models.FoodEntry
import java.text.SimpleDateFormat
import java.util.*

private val mealTypes = listOf("Breakfast", "Lunch", "Dinner", "Snack", "Drink")

/**
 * Simple estimator standing in for NestAI's nutrition estimation.
 * Replace with a real AI/backend call later (see NestAI screen).
 */
private fun estimateNutrition(foodText: String): FoodEntry {
    val words = foodText.split(Regex("[+,]")).map { it.trim() }.filter { it.isNotBlank() }
    val perItemCalories = 120
    val calories = (words.size.coerceAtLeast(1)) * perItemCalories
    return FoodEntry(
        name = foodText,
        servingSize = "1 serving (estimated)",
        calories = calories,
        proteinG = calories / 20,
        carbsG = calories / 8,
        fatG = calories / 30,
        mealType = "Snack",
        timestampMillis = System.currentTimeMillis()
    )
}

@Composable
fun FoodScreen(
    todayFood: List<FoodEntry>,
    onLog: (FoodEntry) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var mealType by remember { mutableStateOf(mealTypes[0]) }
    var pendingEstimate by remember { mutableStateOf<FoodEntry?>(null) }

    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Food & Nutrition", style = MaterialTheme.typography.headlineSmall)

        Text("Meal type", style = MaterialTheme.typography.titleSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            mealTypes.forEach { type ->
                FilterChip(selected = mealType == type, onClick = { mealType = type }, label = { Text(type) })
            }
        }

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("e.g. 2 rotli + dal + curd") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Button(
            onClick = {
                if (text.isNotBlank()) {
                    pendingEstimate = estimateNutrition(text).copy(mealType = mealType)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Estimate nutrition")
        }

        pendingEstimate?.let { entry ->
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Estimated: ${entry.calories} kcal", style = MaterialTheme.typography.titleMedium)
                    Text("Protein ${entry.proteinG}g · Carbs ${entry.carbsG}g · Fat ${entry.fatG}g")
                    Text(
                        "Estimates only — actual values vary by ingredients and portion size.",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Row(Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = {
                            onLog(entry)
                            pendingEstimate = null
                            text = ""
                        }) { Text("Confirm & save") }
                        OutlinedButton(onClick = { pendingEstimate = null }) { Text("Discard") }
                    }
                }
            }
        }

        Divider(Modifier.padding(vertical = 8.dp))
        Text("Today's log", style = MaterialTheme.typography.titleMedium)

        val fmt = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(todayFood) { entry ->
                ListItem(
                    headlineContent = { Text(entry.name) },
                    supportingContent = { Text("${entry.mealType} · ${fmt.format(Date(entry.timestampMillis))}") },
                    trailingContent = { Text("${entry.calories} kcal") }
                )
            }
        }
    }
}
