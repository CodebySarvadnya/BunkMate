package com.example.bunkmate.data

import androidx.room.Entity

@Entity(
    tableName = "attendance_table",
    primaryKeys = ["date", "semester"]
)
data class AttendanceEntity(
    val semester: String,
    val date: String, // Format: "yyyy-MM-dd"
    val lecturesToday: Int,
    val attendedToday: Int
)