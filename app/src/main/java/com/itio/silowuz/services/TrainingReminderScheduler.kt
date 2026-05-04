package com.itio.silowuz.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Scheduler for daily training reminder alarms.
 * Calculates the next occurrence of the selected time (today or tomorrow)
 * and schedules an AlarmManager broadcast for `TrainingReminderReceiver`.
 */
object TrainingReminderScheduler {
    private const val REQUEST_CODE = 7001

    /**
     * Schedules a training reminder alarm for the given time.
     * Uses exact alarm when available, with a safe fallback for restricted devices.
     *
     * @param context Application context used to access AlarmManager
     * @param hour Reminder hour in 24h format (0-23)
     * @param minute Reminder minute (0-59)
     */
    fun schedule(context: Context, hour: Int, minute: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = reminderPendingIntent(context)

        val now = LocalDateTime.now()
        var next = now
            .withHour(hour)
            .withMinute(minute)
            .withSecond(0)
            .withNano(0)

        if (!next.isAfter(now)) {
            next = next.plusDays(1)
        }

        val triggerAtMillis = next
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        try {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            } else {
                // Fallback bez exact - lepsze to niż crash
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            // Ostateczne zabezpieczenie przed wywaleniem appki
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }
    }

    /**
     * Cancels the currently scheduled training reminder alarm.
     *
     * @param context Application context used to access AlarmManager
     */
    fun cancel(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(reminderPendingIntent(context))
    }

    /**
     * Creates a stable PendingIntent used for scheduling and canceling
     * the same reminder alarm.
     *
     * @param context Application context
     * @return Broadcast PendingIntent targeting `TrainingReminderReceiver`
     */
    private fun reminderPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, TrainingReminderReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}