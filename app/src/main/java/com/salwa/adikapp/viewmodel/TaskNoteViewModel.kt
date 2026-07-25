package com.salwa.adikapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.salwa.adikapp.AdikApplication
import com.salwa.adikapp.data.entity.TaskNoteEntity
import com.salwa.adikapp.notification.ReminderScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskNoteViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = (app as AdikApplication).database.taskNoteDao()
    private val context get() = getApplication<AdikApplication>()

    val tasks = dao.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTask(title: String, description: String, courseName: String, deadlineMillis: Long) {
        viewModelScope.launch {
            val id = dao.insert(
                TaskNoteEntity(
                    title = title,
                    description = description,
                    courseName = courseName,
                    deadlineMillis = deadlineMillis
                )
            )
            val saved = TaskNoteEntity(id = id, title = title, description = description, courseName = courseName, deadlineMillis = deadlineMillis, reminderScheduled = true)
            dao.update(saved)
            ReminderScheduler.scheduleReminder(context, saved)
        }
    }

    fun toggleDone(task: TaskNoteEntity) {
        viewModelScope.launch {
            val updated = task.copy(isDone = !task.isDone)
            dao.update(updated)
            if (updated.isDone) ReminderScheduler.cancelReminder(context, task.id)
        }
    }

    fun deleteTask(task: TaskNoteEntity) {
        viewModelScope.launch {
            ReminderScheduler.cancelReminder(context, task.id)
            dao.delete(task)
        }
    }
}
