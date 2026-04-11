package com.itio.silowuz.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import androidx.core.content.edit
import com.itio.silowuz.widget.AppWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/*
    Repository used by PedometerService to update steps taken by the user when tracking is enabled.
    Uses SharedPreferences to store data and singleton pattern to ensure only one instance of the repository.
 */
class StepRepository private constructor(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("pedometer_prefs", Context.MODE_PRIVATE)

    private val repositoryScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val _stepsToday = MutableStateFlow(prefs.getInt("steps_today", 0))
    val stepsToday = _stepsToday.asStateFlow()

    var shouldResetSteps: Boolean = false
    private var _isTrackingCached: Boolean = prefs.getBoolean("is_tracking", false)
    var isTracking : Boolean
        get() = _isTrackingCached
        set(value) {
            _isTrackingCached = value
            prefs.edit { putBoolean("is_tracking", value) }
        }

    /*
        Method checks if tracking is enabled or a direct call was made to reset the last value of steps.
        During the change of day it resets the value of today's steps to 0.
        Calculates steps based on current and last sensor value and adds them to today's total.
     */
    fun updateSteps(context: Context, totalStepsSinceReboot: Int) {
        // Prevents counting steps when user pressed Stop Tracking button on HomeScreen.
        if (!isTracking || shouldResetSteps){
            prefs.edit { putInt("last_steps_sensor_value", totalStepsSinceReboot) }
            shouldResetSteps = false
            return
        }

        val today = LocalDate.now().toString()
        val lastDate = prefs.getString("last_date", "")
        val lastStepsSensorValue = prefs.getInt("last_steps_sensor_value", -1)

        // Sets steps to 0 on new day. Else if last_steps_sensor_value was set it calculates steps
        // taken since last reading. Every 25 steps updates widget.
        if (today != lastDate) {
            _stepsToday.value = 0
            prefs.edit {
                putString("last_date", today)
                putInt("last_steps_sensor_value", totalStepsSinceReboot)
                putInt("steps_today", 0)
            }
        } else if (lastStepsSensorValue != -1) {
            val delta = totalStepsSinceReboot - lastStepsSensorValue
            if (delta > 0) {
                val newTotal = _stepsToday.value + delta
                _stepsToday.value = newTotal
                
                prefs.edit {
                    putInt("steps_today", newTotal)
                    putInt("last_steps_sensor_value", totalStepsSinceReboot)
                }

                if (_stepsToday.value % 25 == 0) {
                    repositoryScope.launch(Dispatchers.IO) {
                        AppWidget().updateStepWidget(context)
                    }
                }
            } else if (delta < 0) {
                prefs.edit { putInt("last_steps_sensor_value", totalStepsSinceReboot) }
            }
        } else {
            prefs.edit { putInt("last_steps_sensor_value", totalStepsSinceReboot) }
        }
    }

    fun getTodaySteps(): Int = _stepsToday.value

    companion object {
        @Volatile
        private var INSTANCE: StepRepository? = null

        fun getInstance(context: Context): StepRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: StepRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
