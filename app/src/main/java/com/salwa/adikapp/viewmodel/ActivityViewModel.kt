package com.salwa.adikapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.salwa.adikapp.AdikApplication
import com.salwa.adikapp.data.entity.ActivityEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ActivityViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = (app as AdikApplication).database.activityDao()

    val activities = dao.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addActivity(title: String, dateMillis: Long, startTime: String, endTime: String, location: String, note: String) {
        viewModelScope.launch {
            dao.insert(ActivityEntity(title = title, dateMillis = dateMillis, startTime = startTime, endTime = endTime, location = location, note = note))
        }
    }

    fun toggleDone(activity: ActivityEntity) {
        viewModelScope.launch { dao.update(activity.copy(isDone = !activity.isDone)) }
    }

    fun deleteActivity(activity: ActivityEntity) {
        viewModelScope.launch { dao.delete(activity) }
    }
}
