package com.salwa.adikapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_notes")
data class TaskNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String = "",
    val courseName: String = "",
    val deadlineMillis: Long,
    val isDone: Boolean = false,
    val reminderScheduled: Boolean = false // penanda notifikasi H-2 sudah dijadwalkan
)
