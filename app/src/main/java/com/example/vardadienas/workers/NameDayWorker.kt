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
                    // TODO: Replace with your actual logic to get today's name day
                    val todaysNameDay = NameDayRepository(this.applicationContext).getNameDayForToday().map { nameDay -> nameDay.name }.toString().replace("[", "").replace("]", "")
                    notifier.showTodaysNameDayNotification(todaysNameDay)
                    rescheduleNextDay()
                }
                TYPE_UPCOMING_NAMEDAY -> {
                    // TODO: This is where you'll add the logic for upcoming name days
                    // val upcomingNameDays = getSubscribedUpcomingNameDays()
                    // notifier.showUpcomingNameDayNotification(upcomingNameDays)
                }
                else -> {
                    // If no type is specified, do nothing or log an error
                }
            }
            Result.success()
        } catch (e: IOException) {
            // e.g., Network error
            Result.retry()
        } catch (e: Exception) {
            Log.e("NameDayWorker", "Error showing notification", e)
            Result.failure()
        }
    }

    private suspend fun rescheduleNextDay() {
        // Read the user's preferred time from DataStore
        val dataStore = SettingsDataStore(applicationContext)
        val time = dataStore.notificationTime.first() // .first() gets the current value

        // Schedule the next worker for tomorrow at the same time
        val scheduler = NotificationScheduler(applicationContext)
        scheduler.scheduleDailyNameDayNotification(hour = time.first, minute = time.second)
    }
}