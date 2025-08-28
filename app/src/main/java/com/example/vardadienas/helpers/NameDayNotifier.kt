package com.example.vardadienas.helpers

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.vardadienas.MainActivity
import com.example.vardadienas.R

// NOTE: Replace R.drawable.ic_notification and MainActivity with your actual resources.
class NameDayNotifier(private val context: Context) {

    companion object {
        const val CHANNEL_ID_NAMEDAY = "nameday_channel"
    }

    // Must be called once when the app starts
    fun createNotificationChannel() {
        val name = "Vārdadienu Paziņojumi"
        val descriptionText = "Paziņojumi par šodienas vārdadienām"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID_NAMEDAY, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @SuppressLint("MissingPermission") // Permission is checked in settings, when notifications are turned on.
    fun showTodaysNameDayNotification(nameDay: String) {
        // Create an intent that opens the app when the notification is tapped
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID_NAMEDAY)
            .setSmallIcon(R.drawable.party_popper) // TODO: Create an icon for the notification
            .setContentTitle("Šodien vārdadienu svin")
            .setContentText(nameDay)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(1, builder.build())
        }
    }
}