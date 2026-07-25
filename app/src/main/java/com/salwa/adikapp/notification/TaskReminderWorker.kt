package com.salwa.adikapp.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class TaskReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val taskId = inputData.getLong(KEY_TASK_ID, -1L)
        val title = inputData.getString(KEY_TITLE) ?: return Result.failure()
        val courseName = inputData.getString(KEY_COURSE) ?: ""

        if (taskId == -1L) return Result.failure()

        NotificationHelper.showDeadlineReminder(applicationContext, taskId, title, courseName)
        return Result.success()
    }

    companion object {
        const val KEY_TASK_ID = "task_id"
        const val KEY_TITLE = "title"
        const val KEY_COURSE = "course"
    }
}
