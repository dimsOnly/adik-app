package com.salwa.adikapp.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.salwa.adikapp.AdikApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val pendingResult = goAsync()
        val app = context.applicationContext as AdikApplication

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val pending = app.database.taskNoteDao().getPendingForReminder()
                pending.forEach { task ->
                    ReminderScheduler.scheduleReminder(context, task)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
