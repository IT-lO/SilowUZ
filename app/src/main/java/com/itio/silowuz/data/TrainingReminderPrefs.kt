package com.itio.silowuz.data

import android.content.Context
import androidx.core.content.edit

class TrainingReminderPrefs(context: Context) {
    private val prefs = context.getSharedPreferences("training_reminder_prefs", Context.MODE_PRIVATE)

    var enabled: Boolean
        get() = prefs.getBoolean("enabled", false)
        set(value) = prefs.edit { putBoolean("enabled", value) }

    var hour: Int
        get() = prefs.getInt("hour", 19)
        set(value) = prefs.edit { putInt("hour", value) }

    var minute: Int
        get() = prefs.getInt("minute", 0)
        set(value) = prefs.edit { putInt("minute", value) }
}