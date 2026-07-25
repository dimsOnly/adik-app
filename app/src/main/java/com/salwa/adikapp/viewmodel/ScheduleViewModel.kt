package com.salwa.adikapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.salwa.adikapp.AdikApplication
import com.salwa.adikapp.data.entity.ClassScheduleEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ScheduleViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = (app as AdikApplication).database.classScheduleDao()

    val schedules = dao.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addSchedule(courseName: String, lecturer: String, room: String, dayOfWeek: Int, startTime: String, endTime: String) {
        viewModelScope.launch {
            dao.insert(ClassScheduleEntity(courseName = courseName, lecturer = lecturer, room = room, dayOfWeek = dayOfWeek, startTime = startTime, endTime = endTime))
        }
    }

    fun deleteSchedule(schedule: ClassScheduleEntity) {
        viewModelScope.launch { dao.delete(schedule) }
    }
}
