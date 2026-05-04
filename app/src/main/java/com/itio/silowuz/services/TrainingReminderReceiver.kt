package com.itio.silowuz.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.itio.silowuz.MainActivity
import com.itio.silowuz.R
import com.itio.silowuz.data.TrainingReminderPrefs
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat


/**
 * BroadcastReceiver responsible for handling scheduled training reminder alarms.
 * Displays the reminder notification and schedules the next daily reminder
 * when reminders are still enabled in user preferences.
 */
class TrainingReminderReceiver : BroadcastReceiver() {

    /**
     * Called by AlarmManager when the reminder alarm is triggered.
     * Creates the notification channel if needed, shows the notification,
     * and schedules the next reminder occurrence.
     *
     * @param context Receiver context
     * @param intent Broadcast intent from AlarmManager
     */
    override fun onReceive(context: Context, intent: Intent?) {
        createChannelIfNeeded(context)
        showNotification(context)

        // Zaplanuj kolejne przypomnienie na następny dzień.
        val prefs = TrainingReminderPrefs(context)
        if (prefs.enabled) {
            TrainingReminderScheduler.schedule(context, prefs.hour, prefs.minute)
        }
    }

    /**
     * Builds and displays the training reminder notification.
     * On Android 13+ it sends the notification only if POST_NOTIFICATIONS
     * permission is granted.
     *
     * @param context Receiver context
     */
    private fun showNotification(context: Context) {
        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            8001,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(context.getString(R.string.training_notification_title))
            .setContentText(context.getString(R.string.training_notification_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
        }

    }

    /**
     * Creates the notification channel used for training reminders
     * on Android O and above.
     *
     * @param context Receiver context
     */
    private fun createChannelIfNeeded(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.training_notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = context.getString(R.string.training_notification_channel_description)
            }

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "training_reminders"
        private const val NOTIFICATION_ID = 8002
    }
}