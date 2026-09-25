package com.fitnest.app.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.fitnest.app.data.models.UserProfile

private val goals = listOf(
    "Improve general fitness",
    "Become more active",
    "Build a consistent workout routine",
    "Track nutrition",
    "Maintain healthy habits"
)
private val activityLevels = listOf("Low", "Moderate", "High")

@Composable
fun ProfileSetupScreen(onSaved: (UserProfile) -> Unit) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var activityLevel by remember { mutableStateOf(activityLevels[1]) }
    var goal by remember { mutableStateOf(goals[0]) }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Let's set up your profile", style = MaterialTheme.typography.headlineSmall)
        Text(
            "This personalizes your dashboard, nutrition targets and workout suggestions.",
            style = MaterialTheme.typography.bodyMedium
        )

        OutlinedTextField(name, { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            age, { age = it.filter { c -> c.isDigit() } },
            label = { Text("Age") },
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            height, { height = it.filter { c -> c.isDigit() } },
            label = { Text("Height (cm)") },
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            weight, { weight = it.filter { c -> c.isDigit() || c == '.' } },
            label = { Text("Weight (kg)") },
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        Text("Activity level", style = MaterialTheme.typography.titleSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            activityLevels.forEach { level ->
                FilterChip(
                    selected = activityLevel == level,
                    onClick = { activityLevel = level },
                    label = { Text(level) }
                )
            }
        }

        Text("Goal", style = MaterialTheme.typography.titleSmall)
        Column {
            goals.forEach { g ->
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = goal == g, onClick = { goal = g })
                    Text(g)
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                onSaved(
                    UserProfile(
                        name = name.ifBlank { "Friend" },
                        age = age.toIntOrNull() ?: 0,
                        heightCm = height.toIntOrNull() ?: 0,
                        weightKg = weight.toFloatOrNull() ?: 0f,
                        activityLevel = activityLevel,
                        goal = goal,
                        profileComplete = true
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save and continue")
        }
    }
}
