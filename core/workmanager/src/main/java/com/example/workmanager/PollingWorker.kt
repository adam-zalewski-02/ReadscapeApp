package com.example.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

class PollingWorker(appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("PollingWorker", "Worker started")
        // Perform polling logic here
        // ...

        // If condition met, send notification
        if (true) {
            delay(5000)
            sendNotification()
        }

        return Result.success()
    }

    private fun sendNotification() {
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
            .setContentTitle("Polling Update")
            .setContentText("Condition met during polling")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationId = 1
        Log.d("PollingWorker", "Sending notification")
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}
