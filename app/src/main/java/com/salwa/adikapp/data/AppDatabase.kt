package com.salwa.adikapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.salwa.adikapp.data.dao.*
import com.salwa.adikapp.data.entity.*

class Converters {
    @TypeConverter
    fun fromTransactionType(type: TransactionType): String = type.name

    @TypeConverter
    fun toTransactionType(value: String): TransactionType = TransactionType.valueOf(value)
}

@Database(
    entities = [
        TransactionEntity::class,
        WishlistEntity::class,
        StudyTargetEntity::class,
        ClassScheduleEntity::class,
        ActivityEntity::class,
        DiaryEntryEntity::class,
        DiaryPhotoEntity::class,
        TaskNoteEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun wishlistDao(): WishlistDao
    abstract fun studyTargetDao(): StudyTargetDao
    abstract fun classScheduleDao(): ClassScheduleDao
    abstract fun activityDao(): ActivityDao
    abstract fun diaryDao(): DiaryDao
    abstract fun taskNoteDao(): TaskNoteDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "adikapp.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
