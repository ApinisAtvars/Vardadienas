package com.example.vardadienas.schedulers

import android.content.Context
import androidx.work.*
import com.example.vardadienas.workers.NameDayWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

class NotificationScheduler(private val context: Context) {

    companion object {
        const val DAILY_NAMEDAY_WORK_TAG = "daily_nameday_work"
    }

    fun scheduleDailyNameDayNotification(hour: Int, minute: Int) {
        val initialDelay = calculateInitialDelay(hour, minute)

        val inputData = workDataOf(
            NameDayWorker.KEY_NOTIFICATION_TYPE to NameDayWorker.TYPE_TODAY_NAMEDAY
        )

        // Use OneTimeWorkRequest for precise timing
        val workRequest = OneTimeWorkRequestBuilder<NameDayWorker>()
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()

        // Use REPLACE policy to cancel the old worker and start a new one if the time changes
        WorkManager.getInstance(context).enqueueUniqueWork(
            DAILY_NAMEDAY_WORK_TAG,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    private fun calculateInitialDelay(hour: Int, minute: Int): Long {
        val now = Calendar.getInstance()
        val scheduledTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // If the scheduled time for today is in the past, schedule it for tomorrow
        if (scheduledTime.before(now)) {
            scheduledTime.add(Calendar.DAY_OF_MONTH, 1)
        }

        return scheduledTime.timeInMillis - now.timeInMillis
    }

    fun cancelDailyNameDayNotification() {
        WorkManager.getInstance(context).cancelUniqueWork(DAILY_NAMEDAY_WORK_TAG)
    }
}