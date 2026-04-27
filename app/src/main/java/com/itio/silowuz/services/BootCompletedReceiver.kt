package com.itio.silowuz.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.itio.silowuz.data.TrainingReminderPrefs

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED) return

        val prefs = TrainingReminderPrefs(context)
        if (prefs.enabled) {
            TrainingReminderScheduler.schedule(context, prefs.hour, prefs.minute)
        }
    }
}