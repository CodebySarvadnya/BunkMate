package com.example.bunkmate.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.bunkmate.data.AttendanceDatabase
import com.example.bunkmate.data.AttendanceEntity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyEntryScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val database = remember { AttendanceDatabase.getDatabase(context) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Semester States
    val semesterList by database.attendanceDao().getAllSemesters().collectAsState(initial = emptyList())
    var selectedSemester by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var showNewSemDialog by remember { mutableStateOf(false) }
    var newSemName by remember { mutableStateOf("") }

    // Input States
    var lecturesToday by remember { mutableStateOf("") }
    var attendedToday by remember { mutableStateOf("") }

    // Date Picker States
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    var showDatePicker by remember { mutableStateOf(false) }
    val formatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val selectedDateText = formatter.format(Date(datePickerState.selectedDateMillis ?: System.currentTimeMillis()))

    // Auto-select first semester if available
    LaunchedEffect(semesterList) {
        if (selectedSemester.isEmpty() && semesterList.isNotEmpty()) {
            selectedSemester = semesterList[0]
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Add Today's Attendance") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // 1. SEMESTER SELECTION ROW
            Row(verticalAlignment = Alignment.CenterVertically) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = if (selectedSemester.isEmpty()) "Select Semester" else selectedSemester,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Semester") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        semesterList.forEach { sem ->
                            DropdownMenuItem(
                                text = { Text(sem) },
                                onClick = {
                                    selectedSemester = sem
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = { showNewSemDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add New Semester")
                }
            }

            // 2. DATE SELECTION CARD
            OutlinedCard(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Selected Date", style = MaterialTheme.typography.labelMedium)
                        Text(selectedDateText, style = MaterialTheme.typography.titleMedium)
                    }
                    Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                }
            }

            // 3. LECTURES INPUTS
            OutlinedTextField(
                value = lecturesToday,
                onValueChange = { if (it.all { char -> char.isDigit() }) lecturesToday = it },
                label = { Text("Total Lectures Today") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            OutlinedTextField(
                value = attendedToday,
                onValueChange = { if (it.all { char -> char.isDigit() }) attendedToday = it },
                label = { Text("Attended Today") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            // 4. SAVE BUTTON
            Button(
                onClick = {
                    val t = lecturesToday.toIntOrNull() ?: 0
                    val a = attendedToday.toIntOrNull() ?: 0

                    if (selectedSemester.isEmpty()) {
                        scope.launch { snackbarHostState.showSnackbar("Please select or create a semester") }
                    } else if (t > 0 && a <= t) {
                        scope.launch {
                            // Find the latest record FOR THE SELECTED SEMESTER to keep cumulative count
                            val lastRecord = database.attendanceDao().getLatestRecordBySemester(selectedSemester)

                            val newTotal = (lastRecord?.lecturesToday ?: 0) + t
                            val newAttended = (lastRecord?.attendedToday ?: 0) + a

                            database.attendanceDao().insertRecord(
                                AttendanceEntity(
                                    date = selectedDateText,
                                    semester = selectedSemester,
                                    lecturesToday = newTotal,
                                    attendedToday = newAttended
                                )
                            )
                            snackbarHostState.showSnackbar("Saved to $selectedSemester")
                            lecturesToday = ""
                            attendedToday = ""
                        }
                    } else {
                        scope.launch { snackbarHostState.showSnackbar("Invalid input") }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Save Attendance")
            }
        }


        // New Semester Dialog
        if (showNewSemDialog) {
            AlertDialog(
                onDismissRequest = { showNewSemDialog = false },
                title = { Text("Create New Semester") },
                text = {
                    OutlinedTextField(
                        value = newSemName,
                        onValueChange = { newSemName = it },
                        label = { Text("Semester Name (e.g. Sem 3)") }
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        if (newSemName.isNotBlank()) {
                            selectedSemester = newSemName
                            showNewSemDialog = false
                            newSemName = ""
                        }
                    }) { Text("Create") }
                },
                dismissButton = {
                    TextButton(onClick = { showNewSemDialog = false }) { Text("Cancel") }
                }
            )
        }

        // Date Picker Dialog
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = { showDatePicker = false }) { Text("OK") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}