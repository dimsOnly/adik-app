package com.salwa.adikapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val dateMillis: Long,
    val startTime: String = "",
    val endTime: String = "",
    val location: String = "",
    val note: String = "",
    val isDone: Boolean = false
)
