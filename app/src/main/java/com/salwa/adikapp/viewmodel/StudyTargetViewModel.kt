package com.salwa.adikapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.salwa.adikapp.AdikApplication
import com.salwa.adikapp.data.entity.StudyTargetEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StudyTargetViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = (app as AdikApplication).database.studyTargetDao()

    val targets = dao.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTarget(title: String, description: String, targetDateMillis: Long?) {
        viewModelScope.launch {
            dao.insert(StudyTargetEntity(title = title, description = description, targetDateMillis = targetDateMillis))
        }
    }

    fun updateProgress(target: StudyTargetEntity, progress: Int) {
        viewModelScope.launch {
            dao.update(target.copy(progressPercent = progress, isDone = progress >= 100))
        }
    }

    fun deleteTarget(target: StudyTargetEntity) {
        viewModelScope.launch { dao.delete(target) }
    }
}
