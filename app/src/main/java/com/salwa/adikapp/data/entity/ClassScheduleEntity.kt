package com.salwa.adikapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// dayOfWeek: 1 = Senin ... 7 = Minggu
@Entity(tableName = "class_schedule")
data class ClassScheduleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val courseName: String,
    val lecturer: String = "",
    val room: String = "",
    val dayOfWeek: Int,
    val startTime: String, // format "HH:mm"
    val endTime: String
)
