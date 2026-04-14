package com.example.bunkmate.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: AttendanceEntity)

    @Delete
    suspend fun deleteRecord(record: AttendanceEntity)

    @Query("SELECT * FROM attendance_table WHERE semester = :sem ORDER BY date DESC LIMIT 1")
    suspend fun getLatestRecordBySemester(sem: String): AttendanceEntity?

    @Query("SELECT * FROM attendance_table WHERE semester = :sem ORDER BY date DESC")
    fun getRecordsBySemester(sem: String): Flow<List<AttendanceEntity>>

    // Optional: Get a list of all unique semesters entered
    @Query("SELECT DISTINCT semester FROM attendance_table ORDER BY semester ASC")
    fun getAllSemesters(): Flow<List<String>>
}