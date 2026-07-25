package com.salwa.adikapp

import android.app.Application
import androidx.work.Configuration
import com.salwa.adikapp.data.AppDatabase
import com.salwa.adikapp.notification.NotificationHelper

class AdikApplication : Application(), Configuration.Provider {

    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }

    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createChannel(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().build()
}
