package com.salwa.adikapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist")
data class WishlistEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val estimatedPrice: Long = 0,
    val priority: Int = 1, // 1 = biasa, 2 = penting, 3 = sangat ingin
    val isAchieved: Boolean = false,
    val note: String = "",
    val createdAtMillis: Long
)
