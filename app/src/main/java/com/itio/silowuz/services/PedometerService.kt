package com.itio.silowuz.services

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.itio.silowuz.R
import com.itio.silowuz.data.StepRepository

/**
 * Foreground service used to count user's steps based on the step counter sensor.
 */
class PedometerService : Service(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var stepCounterSensor: Sensor? = null
    private lateinit var stepRepository: StepRepository

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepRepository = StepRepository.getInstance(this)
        stepRepository.shouldResetSteps = true
        
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification(stepRepository.getTodaySteps()))
        
        if (hasPermission()) {
            startTracking()
        }
    }

    /**
     * Checks if the app has permission to use the step counter sensor.
     * @return Boolean indicating if the permission is granted
     */
    private fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Registers the listener for step counter sensor.
     */
    private fun startTracking() {
        if (stepCounterSensor != null){
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    /**
     * When step sensor gets updated it sends the value to the repository and creates
     * a notification for amount of steps taken. Foreground services are required to display notification
     * to inform user about running service.
     * @param event SensorEvent containing the updated value
     */
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val totalStepsSinceReboot = event.values[0].toInt()
            stepRepository.updateSteps(this, totalStepsSinceReboot)

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID, createNotification(stepRepository.getTodaySteps()))
        }
    }

    override fun onBind(p0: Intent?): IBinder? {return null}

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    /**
     * Creates notification with current amount of steps taken.
     * @param steps Current amount of steps taken
     * @return Notification object
     */
    private fun createNotification(steps: Int): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("SilowUZ")
            .setContentText("Steps: $steps")
            .setSmallIcon(R.drawable.exercise_ico)
            .setOngoing(true)
            .build()
    }

    /**
     * Prepares the channel to show the notification.
     */
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Pedometer Service",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    companion object {
        private const val CHANNEL_ID = "pedometer_channel"
        private const val NOTIFICATION_ID = 1
    }
}
