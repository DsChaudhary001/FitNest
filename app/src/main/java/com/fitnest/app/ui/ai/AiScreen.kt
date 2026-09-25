package com.fitnest.app.ui.ai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class ChatMessage(val fromUser: Boolean, val text: String)

/**
 * NestAI placeholder: simple rule-based responder so the app is fully
 * functional offline. Swap replyTo() for a real API call (see the
 * project README for wiring a backend + LLM safely).
 */
private fun replyTo(input: String): String {
    val text = input.lowercase()
    return when {
        "minute" in text || "time" in text ->
            "With that time available, try a short home workout from the Workout tab — the 5 and 10 minute options are a good fit."
        "ate" in text || "had" in text || "food" in text || "lunch" in text || "dinner" in text || "breakfast" in text ->
            "Head to the Food tab to log that — enter it there and I'll estimate the nutrition for you to confirm."
        "progress" in text || "week" in text ->
            "Your workout and logging streaks are on the Progress tab, updated from what you've tracked so far."
        "water" in text ->
            "You can log water quickly from the Home tab's quick actions."
        "hurt" in text || "pain" in text || "injury" in text || "sick" in text ->
            "I'm not able to give medical advice. For pain, injury, or illness, please check with a doctor or qualified healthcare professional."
        else ->
            "I can help with food logging, workout suggestions, and your daily summaries. What would you like to do?"
    }
}

@Composable
fun AiScreen() {
    var messages by remember {
        mutableStateOf(
            listOf(ChatMessage(false, "Hi! I'm NestAI. Tell me how much time you have, or what you ate, and I'll help."))
        )
    }
    var input by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("NestAI", style = MaterialTheme.typography.headlineSmall)
        Text(
            "General fitness guidance only — not a substitute for medical advice.",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.height(8.dp))

        LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(messages) { msg ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = if (msg.fromUser) Arrangement.End else Arrangement.Start
                ) {
                    ElevatedCard {
                        Text(msg.text, Modifier.padding(12.dp))
                    }
                }
            }
        }

        Row(
            Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                label = { Text("Ask NestAI") }
            )
            Button(onClick = {
                if (input.isNotBlank()) {
                    val userMsg = ChatMessage(true, input)
                    val reply = ChatMessage(false, replyTo(input))
                    messages = messages + userMsg + reply
                    input = ""
                }
            }) { Text("Send") }
        }
    }
}
