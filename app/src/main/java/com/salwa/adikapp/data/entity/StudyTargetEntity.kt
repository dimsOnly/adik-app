package com.salwa.adikapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_targets")
data class StudyTargetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String = "",
    val targetDateMillis: Long? = null,
    val isDone: Boolean = false,
    val progressPercent: Int = 0
)
