package com.example.vardadienas.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.vardadienas.data.repositories.NameDayRepository
import com.example.vardadienas.data.stores.SettingsDataStore
import com.example.vardadienas.helpers.NameDayNotifier
import com.example.vardadienas.schedulers.NotificationScheduler
import kotlinx.coroutines.flow.first
import java.io.IOException

class NameDayWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        // Input data key to make the worker reusable
        const val KEY_NOTIFICATION_TYPE = "notification_type"
        const val TYPE_TODAY_NAMEDAY = "today_nameday"
        const val TYPE_UPCOMING_NAMEDAY = "upcoming_nameday" // For future use
    }

    override suspend fun doWork(): Result {
        val notifier = NameDayNotifier(applicationContext)
        val notificationType = inputData.getString(KEY_NOTIFICATION_TYPE)

        return try {
            when (notificationType) {
                TYPE_TODAY_NAMEDAY -> {
                    val todaysNameDay = NameDayRepository(this.applicationContext).getNameDayForToday().map { nameDay -> nameDay.name }.toString().replace("[", "").replace("]", "")
                    if (todaysNameDay.isNotBlank()) {
                        notifier.showTodaysNameDayNotification(todaysNameDay)
                    }
                }
            }
            Result.success()
        } catch (e: Exception) {
            Log.e("NameDayWorker", "Error showing notification", e)
            Result.failure()
        }
    }
}