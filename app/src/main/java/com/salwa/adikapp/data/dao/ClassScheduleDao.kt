package com.salwa.adikapp.data.dao

import androidx.room.*
import com.salwa.adikapp.data.entity.ClassScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassScheduleDao {
    @Query("SELECT * FROM class_schedule ORDER BY dayOfWeek ASC, startTime ASC")
    fun getAll(): Flow<List<ClassScheduleEntity>>

    @Insert
    suspend fun insert(schedule: ClassScheduleEntity): Long

    @Update
    suspend fun update(schedule: ClassScheduleEntity)

    @Delete
    suspend fun delete(schedule: ClassScheduleEntity)
}
