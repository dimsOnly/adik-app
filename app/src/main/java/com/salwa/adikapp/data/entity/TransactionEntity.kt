package com.salwa.adikapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TransactionType { PEMASUKAN, PENGELUARAN }

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val amount: Long, // dalam rupiah
    val type: TransactionType,
    val category: String,
    val note: String = "",
    val dateMillis: Long
)
