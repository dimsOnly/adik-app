package com.salwa.adikapp.data.dao

import androidx.room.*
import com.salwa.adikapp.data.entity.ActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activities ORDER BY dateMillis ASC, startTime ASC")
    fun getAll(): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM activities WHERE dateMillis BETWEEN :startOfDay AND :endOfDay ORDER BY startTime ASC")
    fun getForDay(startOfDay: Long, endOfDay: Long): Flow<List<ActivityEntity>>

    @Insert
    suspend fun insert(activity: ActivityEntity): Long

    @Update
    suspend fun update(activity: ActivityEntity)

    @Delete
    suspend fun delete(activity: ActivityEntity)
}
