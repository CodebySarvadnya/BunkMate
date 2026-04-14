package com.example.bunkmate.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bunkmate.data.AttendanceDatabase
import com.example.bunkmate.data.AttendanceEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val database = remember { AttendanceDatabase.getDatabase(context) }
    val scope = rememberCoroutineScope()

    // 1. Get the list of unique semesters from DB for the dropdown
    val semesterList by database.attendanceDao().getAllSemesters().collectAsState(initial = emptyList())

    // 2. State for the dropdown selection
    var selectedSemester by remember { mutableStateOf("Semester 1") }
    var expanded by remember { mutableStateOf(false) }

    // Update selection if list loads and current selection is not in it
    LaunchedEffect(semesterList) {
        if (semesterList.isNotEmpty() && selectedSemester !in semesterList) {
            selectedSemester = semesterList[0]
        }
    }

    // 3. Collect history filtered by the selected semester
    val historyList by database.attendanceDao()
        .getRecordsBySemester(selectedSemester)
        .collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Attendance History") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // SEMESTER DROPDOWN SELECTOR
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = selectedSemester,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Semester") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    if (semesterList.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("No semesters found") },
                            onClick = { expanded = false }
                        )
                    } else {
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
            }

            // HISTORY LIST
            if (historyList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No records for $selectedSemester",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(historyList) { record ->
                        AttendanceItem(
                            record = record,
                            onDelete = {
                                scope.launch {
                                    database.attendanceDao().deleteRecord(record)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AttendanceItem(record: AttendanceEntity, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = record.date, style = MaterialTheme.typography.labelSmall)
                Text(
                    text = "Total: ${record.attendedToday} / ${record.lecturesToday}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            val percentage = if (record.lecturesToday > 0) {
                (record.attendedToday.toDouble() / record.lecturesToday * 100)
            } else 0.0

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${String.format("%.1f", percentage)}%",
                    style = MaterialTheme.typography.titleLarge,
                    color = if (percentage >= 75) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}