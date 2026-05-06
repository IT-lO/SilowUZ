package com.itio.silowuz.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.itio.silowuz.data.TrainingReminderPrefs

/**
 * BroadcastReceiver that restores training reminder scheduling after device reboot.
 * Since AlarmManager alarms are cleared on reboot, this receiver reads saved
 * reminder preferences and schedules the next alarm again if reminders are enabled.
 */
class BootCompletedReceiver : BroadcastReceiver() {

    /**
     * Handles boot completion broadcasts and reschedules reminder alarms.
     * Supports both regular and locked boot completion actions.
     *
     * @param context Receiver context
     * @param intent System broadcast intent
     */
    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action ?: return
        if (action != Intent.ACTION_BOOT_COMPLETED &&
            action != Intent.ACTION_LOCKED_BOOT_COMPLETED
        ) return

        val prefs = TrainingReminderPrefs(context)
        if (prefs.enabled) {
            TrainingReminderScheduler.schedule(context, prefs.hour, prefs.minute)
        }
    }
}