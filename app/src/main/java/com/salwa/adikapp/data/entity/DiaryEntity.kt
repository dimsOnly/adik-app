package com.salwa.adikapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "diary_entries")
data class DiaryEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateMillis: Long,
    val title: String = "",
    val content: String,
    val mood: String = "" // opsional: emoji/label mood hari itu
)

@Entity(
    tableName = "diary_photos",
    foreignKeys = [
        ForeignKey(
            entity = DiaryEntryEntity::class,
            parentColumns = ["id"],
            childColumns = ["diaryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DiaryPhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val diaryId: Long,
    val filePath: String // path lokal foto tersimpan di internal storage
)
