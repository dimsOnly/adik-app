package com.salwa.adikapp.notification

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.salwa.adikapp.data.entity.TaskNoteEntity
import java.util.concurrent.TimeUnit

object ReminderScheduler {

    private const val TWO_DAYS_MILLIS = 2 * 24 * 60 * 60 * 1000L

    /**
     * Menjadwalkan notifikasi H-2 sebelum deadline tugas.
     * Jika waktu H-2 sudah lewat (misal tugas dibuat kurang dari 2 hari sebelum deadline),
     * notifikasi akan langsung dijadwalkan secepatnya (delay minimal).
     */
    fun scheduleReminder(context: Context, task: TaskNoteEntity) {
        val triggerAt = task.deadlineMillis - TWO_DAYS_MILLIS
        val now = System.currentTimeMillis()
        val delay = (triggerAt - now).coerceAtLeast(0L)

        val data = Data.Builder()
            .putLong(TaskReminderWorker.KEY_TASK_ID, task.id)
            .putString(TaskReminderWorker.KEY_TITLE, task.title)
            .putString(TaskReminderWorker.KEY_COURSE, task.courseName)
            .build()

        val request = OneTimeWorkRequestBuilder<TaskReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag(reminderTag(task.id))
            .build()

        WorkManager.getInstance(context).enqueue(request)
    }

    fun cancelReminder(context: Context, taskId: Long) {
        WorkManager.getInstance(context).cancelAllWorkByTag(reminderTag(taskId))
    }

    private fun reminderTag(taskId: Long) = "task_reminder_$taskId"
}
