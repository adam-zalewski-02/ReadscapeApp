package com.example.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.data.repository.DefaultUserRepository

class PollingWorker(appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {

    lateinit var userRepository: DefaultUserRepository
    override suspend fun doWork(): Result {
        Log.d("PollingWorker", "Worker started")

        val sensorKitResult = userRepository.getSensorKit("fdqsf")
        sensorKitResult.onSuccess { sensorKit ->
            if (sensorKit.condition == "dangerous") {
                sendNotification()
            }
        }

        return Result.success()
    }

    fun sendNotification() {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        Log.d("PollingWorker", "NotificationManager: $notificationManager")

        // Create Notification Channel for API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Polling Channel"
            val descriptionText = "Channel for Polling Notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("POLLING_CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(applicationContext, "POLLING_CHANNEL_ID")
            .setContentTitle("Sensorkit Warning from workmanager")
            .setContentText("Room conditions are dangerous! (from workmanager)")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationId = 1
        Log.d("PollingWorker", "Sending notification")
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}


