package com.salwa.adikapp.data.dao

import androidx.room.*
import com.salwa.adikapp.data.entity.StudyTargetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyTargetDao {
    @Query("SELECT * FROM study_targets ORDER BY isDone ASC, targetDateMillis ASC")
    fun getAll(): Flow<List<StudyTargetEntity>>

    @Insert
    suspend fun insert(target: StudyTargetEntity): Long

    @Update
    suspend fun update(target: StudyTargetEntity)

    @Delete
    suspend fun delete(target: StudyTargetEntity)
}
