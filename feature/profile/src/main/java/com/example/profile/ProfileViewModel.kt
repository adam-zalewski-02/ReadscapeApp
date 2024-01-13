package com.example.profile

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.workmanager.PollingWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import com.example.workmanager.PollingForegroundService

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) : ViewModel() {
    init {
        val pollingWork = PeriodicWorkRequestBuilder<PollingWorker>(10, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(pollingWork)
        val startIntent = Intent(applicationContext, PollingForegroundService::class.java)
        applicationContext.startForegroundService(startIntent)

    }
}