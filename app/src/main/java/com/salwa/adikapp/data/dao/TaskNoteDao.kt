package com.salwa.adikapp.data.dao

import androidx.room.*
import com.salwa.adikapp.data.entity.TaskNoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskNoteDao {
    @Query("SELECT * FROM task_notes ORDER BY isDone ASC, deadlineMillis ASC")
    fun getAll(): Flow<List<TaskNoteEntity>>

    @Query("SELECT * FROM task_notes WHERE reminderScheduled = 0 AND isDone = 0")
    suspend fun getPendingForReminder(): List<TaskNoteEntity>

    @Insert
    suspend fun insert(task: TaskNoteEntity): Long

    @Update
    suspend fun update(task: TaskNoteEntity)

    @Delete
    suspend fun delete(task: TaskNoteEntity)
}
