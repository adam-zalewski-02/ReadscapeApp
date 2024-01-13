package com.example.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.workmanager.PollingWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) : ViewModel() {
    init {
        val pollingWork = OneTimeWorkRequestBuilder<PollingWorker>().build()
        WorkManager.getInstance(applicationContext).enqueue(pollingWork)
    }
}