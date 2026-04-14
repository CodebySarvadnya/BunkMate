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
fun CalculatorScreen(
    onBackClick: () -> Unit = {}
) {
    var totalLectures by remember { mutableStateOf("") }
    var attendedLectures by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }

    val calculator = AttendanceCalculator()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calculate Attendance") },
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
                text = "Enter your lecture details below",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = attendedLectures,
                onValueChange = { if (it.all { char -> char.isDigit() }) attendedLectures = it },
                label = { Text("Attended Lectures") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            OutlinedTextField(
                value = totalLectures,
                onValueChange = { if (it.all { char -> char.isDigit() }) totalLectures = it },
                label = { Text("Total Lectures") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val total = totalLectures.toIntOrNull() ?: 0
                    val attended = attendedLectures.toIntOrNull() ?: 0

                    if (total > 0 && attended <= total) {
                        val percentage = calculator.calculateAttendance(attended, total)
                        resultText = "Your Attendance: $percentage%"
                    } else if (attended > total) {
                        resultText = "Attended cannot be more than Total"
                    } else {
                        resultText = "Please enter valid numbers"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Calculate", fontSize = 18.sp)
            }

            if (resultText.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Box(
                        modifier = Modifier.padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = resultText,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }
    }
}