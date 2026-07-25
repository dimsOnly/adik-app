package com.salwa.adikapp.data.dao

import androidx.room.*
import com.salwa.adikapp.data.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY dateMillis DESC")
    fun getAll(): Flow<List<TransactionEntity>>

    @Query("SELECT COALESCE(SUM(CASE WHEN type = 'PEMASUKAN' THEN amount ELSE -amount END), 0) FROM transactions")
    fun getSaldo(): Flow<Long>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'PEMASUKAN'")
    fun getTotalPemasukan(): Flow<Long>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'PENGELUARAN'")
    fun getTotalPengeluaran(): Flow<Long>

    @Insert
    suspend fun insert(transaction: TransactionEntity): Long

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)
}
