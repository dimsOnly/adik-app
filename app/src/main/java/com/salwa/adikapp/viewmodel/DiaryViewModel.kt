package com.salwa.adikapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.salwa.adikapp.AdikApplication
import com.salwa.adikapp.data.entity.DiaryEntryEntity
import com.salwa.adikapp.data.entity.DiaryPhotoEntity
import com.salwa.adikapp.util.PhotoStorage
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DiaryViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = (app as AdikApplication).database.diaryDao()

    val entries = dao.getAllWithPhotos().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addEntry(title: String, content: String, mood: String, dateMillis: Long, photoPaths: List<String>) {
        viewModelScope.launch {
            val entryId = dao.insertEntry(DiaryEntryEntity(dateMillis = dateMillis, title = title, content = content, mood = mood))
            photoPaths.forEach { path ->
                dao.insertPhoto(DiaryPhotoEntity(diaryId = entryId, filePath = path))
            }
        }
    }

    fun deleteEntry(entry: DiaryEntryEntity, photoPaths: List<String>) {
        viewModelScope.launch {
            dao.deleteEntry(entry)
            photoPaths.forEach { PhotoStorage.deletePhoto(it) }
        }
    }
}
