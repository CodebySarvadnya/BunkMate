package com.example.bunkmate.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Ensure this import matches the package where you created AttendanceCalculator.kt
import com.example.bunkmate.logic.AttendanceCalculator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PredictionScreen(
    onBackClick: () -> Unit = {}
) {
    // Current Stats
    var attendedInput by remember { mutableStateOf("") }
    var totalInput by remember { mutableStateOf("") }

    // Future Plans
    var willAttendInput by remember { mutableStateOf("") }
    var willMissInput by remember { mutableStateOf("") }

    var predictionResult by remember { mutableStateOf("") }

    val calculator = AttendanceCalculator()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Predict Future Attendance") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Plan your upcoming lectures",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )

            // Current Stats Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = attendedInput,
                    onValueChange = { if (it.all { c -> c.isDigit() }) attendedInput = it },
                    label = { Text("Attended") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = totalInput,
                    onValueChange = { if (it.all { c -> c.isDigit() }) totalInput = it },
                    label = { Text("Total") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "Future Estimates",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )

            OutlinedTextField(
                value = willAttendInput,
                onValueChange = { if (it.all { c -> c.isDigit() }) willAttendInput = it },
                label = { Text("Lectures you WILL attend") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = willMissInput,
                onValueChange = { if (it.all { c -> c.isDigit() }) willMissInput = it },
                label = { Text("Lectures you WILL miss (Bunk)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val attended = attendedInput.toIntOrNull() ?: 0
                    val total = totalInput.toIntOrNull() ?: 0
                    val willAttend = willAttendInput.toIntOrNull() ?: 0
                    val willMiss = willMissInput.toIntOrNull() ?: 0

                    if (total > 0 && attended <= total) {
                        val futurePercentage = calculator.futureAttendance(
                            attended, total, willAttend, willMiss
                        )
                        predictionResult = "Predicted Attendance: $futurePercentage%"
                    } else {
                        predictionResult = "Please enter valid current stats"
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Predict Percentage", fontSize = 16.sp)
            }

            if (predictionResult.isNotEmpty()) {
                Card(
                    modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = predictionResult,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }
    }
}