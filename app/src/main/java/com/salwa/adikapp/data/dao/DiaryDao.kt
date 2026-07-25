package com.salwa.adikapp.data.dao

import androidx.room.*
import com.salwa.adikapp.data.entity.DiaryEntryEntity
import com.salwa.adikapp.data.entity.DiaryPhotoEntity
import kotlinx.coroutines.flow.Flow

data class DiaryWithPhotos(
    @Embedded val entry: DiaryEntryEntity,
    @Relation(parentColumn = "id", entityColumn = "diaryId")
    val photos: List<DiaryPhotoEntity>
)

@Dao
interface DiaryDao {
    @Transaction
    @Query("SELECT * FROM diary_entries ORDER BY dateMillis DESC")
    fun getAllWithPhotos(): Flow<List<DiaryWithPhotos>>

    @Insert
    suspend fun insertEntry(entry: DiaryEntryEntity): Long

    @Update
    suspend fun updateEntry(entry: DiaryEntryEntity)

    @Delete
    suspend fun deleteEntry(entry: DiaryEntryEntity)

    @Insert
    suspend fun insertPhoto(photo: DiaryPhotoEntity): Long

    @Delete
    suspend fun deletePhoto(photo: DiaryPhotoEntity)
}
