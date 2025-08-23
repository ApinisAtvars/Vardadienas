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

    fun scheduleDailyNameDayNotification() {
        val initialDelay = calculateInitialDelayToNineAm()

        val inputData = workDataOf(
            NameDayWorker.KEY_NOTIFICATION_TYPE to NameDayWorker.TYPE_TODAY_NAMEDAY
        )

        // This is the core of the new strategy
        val workRequest = PeriodicWorkRequestBuilder<NameDayWorker>(
            24, TimeUnit.HOURS, // Repeat every 24 hours
            2, TimeUnit.HOURS   // Flex time: run within the last 2 hours of the interval
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()

        // Use KEEP policy: If work is already scheduled, do nothing.
        // This prevents rescheduling every time the user opens the settings screen.
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            DAILY_NAMEDAY_WORK_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    private fun calculateInitialDelayToNineAm(): Long {
        val now = Calendar.getInstance()
        val nineAmToday = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // If it's already past 9 AM today, schedule for 9 AM tomorrow.
        if (nineAmToday.before(now)) {
            nineAmToday.add(Calendar.DAY_OF_MONTH, 1)
        }

        return nineAmToday.timeInMillis - now.timeInMillis
    }

    fun cancelDailyNameDayNotification() {
        WorkManager.getInstance(context).cancelUniqueWork(DAILY_NAMEDAY_WORK_TAG)
    }
}