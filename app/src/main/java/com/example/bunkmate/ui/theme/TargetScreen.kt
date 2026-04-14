package com.example.bunkmate.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bunkmate.logic.AttendanceCalculator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TargetScreen(
    onBackClick: () -> Unit = {}
) {
    // State for inputs
    var attendedInput by remember { mutableStateOf("") }
    var totalInput by remember { mutableStateOf("") }
    var targetInput by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }

    val calculator = AttendanceCalculator()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Set Target Attendance") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Find out how many more classes you need.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = attendedInput,
                onValueChange = { if (it.all { c -> c.isDigit() }) attendedInput = it },
                label = { Text("Current Attended") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            OutlinedTextField(
                value = totalInput,
                onValueChange = { if (it.all { c -> c.isDigit() }) totalInput = it },
                label = { Text("Current Total Lectures") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            OutlinedTextField(
                value = targetInput,
                onValueChange = {
                    // Allow digits and one decimal point
                    if (it.isEmpty() || it.toDoubleOrNull() != null || it.endsWith(".")) {
                        targetInput = it
                    }
                },
                label = { Text("Target Percentage (e.g. 75)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )

            Button(
                onClick = {
                    val attended = attendedInput.toIntOrNull() ?: 0
                    val total = totalInput.toIntOrNull() ?: 0
                    val target = targetInput.toDoubleOrNull() ?: 0.0

                    if (target > 100) {
                        resultText = "Target cannot be over 100%"
                    } else if (total > 0 && target > 0) {
                        val currentPercentage = (attended.toDouble() / total) * 100

                        resultText = if (currentPercentage >= target) {
                            val canBunk = calculator.lecturesCanBunk(attended, total, target)
                            if (canBunk > 0) {
                                "You are above target! You can bunk the next $canBunk lectures and still stay above $target%."
                            } else {
                                "You are exactly on target! You cannot bunk any more lectures."
                            }
                        } else {
                            val needed = calculator.lecturesNeededForTarget(attended, total, target)
                            if (needed == -1) {
                                "It is mathematically impossible to reach this target."
                            } else {
                                "You need to attend $needed more lectures consecutively to reach $target%."
                            }
                        }
                    } else {
                        resultText = "Please enter valid numbers"
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Calculate Needed / Bunkable", fontSize = 16.sp)
            }

            if (resultText.isNotEmpty()) {
                Card(
                    modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Text(
                        text = resultText,
                        modifier = Modifier.padding(20.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }
    }
}