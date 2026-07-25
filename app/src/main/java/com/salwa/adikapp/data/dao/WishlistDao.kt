package com.salwa.adikapp.data.dao

import androidx.room.*
import com.salwa.adikapp.data.entity.WishlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {
    @Query("SELECT * FROM wishlist ORDER BY priority DESC, createdAtMillis DESC")
    fun getAll(): Flow<List<WishlistEntity>>

    @Insert
    suspend fun insert(item: WishlistEntity): Long

    @Update
    suspend fun update(item: WishlistEntity)

    @Delete
    suspend fun delete(item: WishlistEntity)
}
