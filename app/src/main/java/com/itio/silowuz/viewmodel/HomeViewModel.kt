package com.itio.silowuz.viewmodel

import android.Manifest
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.snapshotFlow
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.BarEntry
import com.itio.silowuz.data.PedometerFirebaseRepository
import com.itio.silowuz.data.StepRepository
import com.itio.silowuz.dataclass.home.DailySteps
import com.itio.silowuz.dataclass.home.HomeUiState
import com.itio.silowuz.services.PedometerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val stepRepository = StepRepository.getInstance(application)
    private val pedometerFirebaseRepository = PedometerFirebaseRepository()
    private val _uiState = MutableStateFlow(HomeUiState(
        userName = getUserName(),
        dateString = getCurrentDate(),
        steps = stepRepository.getTodaySteps(),
        stepGoal = getStepGoal(),
        calories = calculateCaloriesBurned(stepRepository.getTodaySteps()),
        distanceKm = calculateTraversedDistanceKm(stepRepository.getTodaySteps()),
        streakDays = getActivityStreak(),
        activeMinutes = getActivityMinutes(),
        isTracking = false,
        barEntries = emptyList()
    ))
    val uiState = _uiState.asStateFlow()

    /*
        When ViewModel launches it creates listeners to data from repositories.
        Whenever this data changes it updates the UI state.
     */
    init {
        viewModelScope.launch {
            stepRepository.stepsToday.collect { steps ->
                _uiState.update { currentState ->
                    currentState.copy(
                        steps = steps,
                        calories = calculateCaloriesBurned(steps),
                        distanceKm = calculateTraversedDistanceKm(steps)
                    )
                }
            }
        }
        viewModelScope.launch {
            snapshotFlow { pedometerFirebaseRepository.weeklySteps }.collect { weeklyList ->
                _uiState.update { currentState ->
                    currentState.copy(
                        barEntries = convertWeeklyStepsToBarEntries(weeklyList)
                    )
                }
            }
        }
    }

    /*
        Turns on/off PedometerService to track steps. In case of missing permissions,
        when tracking is turned on, onPermissionRequired is called with the missing permissions.
     */
    fun toggleTracking(onPermissionRequired: (Array<String>) -> Unit) {
        val context = getApplication<Application>()
        val intent = Intent(context, PedometerService::class.java)
        
        if (_uiState.value.isTracking) {
            context.stopService(intent)
            stepRepository.isTracking = false
            _uiState.update { it.copy(isTracking = false) }
        } else {
            val missingPermissions = getMissingPermissions()
            if (missingPermissions.isNotEmpty()) {
                onPermissionRequired(missingPermissions.toTypedArray())
            } else {
                context.startForegroundService(intent)
                stepRepository.isTracking = true
                _uiState.update { it.copy(isTracking = true) }
            }
        }
    }

    /*
        Get permissions to post notifications and track activity required for PedometerService.
     */
    private fun getMissingPermissions(): List<String> {
        val missing = mutableListOf<String>()
        val context = getApplication<Application>()

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACTIVITY_RECOGNITION)
            != PackageManager.PERMISSION_GRANTED) {
            missing.add(Manifest.permission.ACTIVITY_RECOGNITION)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                missing.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        return missing
    }

    /*
        Starts PedometerService after permissions have been granted.
     */
    fun startTrackingAfterPermission() {
        val context = getApplication<Application>()
        val intent = Intent(context, PedometerService::class.java)
        context.startForegroundService(intent)
        stepRepository.isTracking = true
        _uiState.update { it.copy(isTracking = true) }
    }

    fun getUserName() : String {
        return "Użytkownik"
    }

    fun getCurrentDate() : String{
        val locale = Locale("pl", "PL")
        val formater = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", locale)
        return LocalDate.now().format(formater)
    }

    fun getStepGoal() : Int{
        return 10000
    }

    fun calculateCaloriesBurned(steps: Int) : Int{
        return (steps * 0.04).roundToInt()
    }

    fun calculateTraversedDistanceKm(steps: Int) : Double{
        return (steps * 0.7 / 1000).toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
    }

    fun getActivityStreak() : Int {
        return 7
    }

    fun getActivityMinutes() : Int {
        return 45
    }

    /*
        Converts the weekly steps from PedometerFirebaseRepository to BarEntries for the chart
        used in HomeScreen.
     */
    fun convertWeeklyStepsToBarEntries(weeklySteps: List<DailySteps>): List<BarEntry>{
        val barEntriesList = ArrayList<BarEntry>()

        val stepsMap = weeklySteps.associate {
            val date = LocalDate.parse(it.day)
            date.dayOfWeek.value to it.stepCount.toFloat()
        }

        for (i in 1..7) {
            val steps = stepsMap[i] ?: 0f
            barEntriesList.add(BarEntry((i - 1).toFloat(), steps))
        }

        return barEntriesList
    }

    /*
        Test function used for debugging. Delete this and the test button from UI before merging.
     */
    fun saveDailySteps(stepCount: Int) {
        val day = LocalDate.now().toString()
//        val day = LocalDate.of(2026,4,14).toString()
        pedometerFirebaseRepository.saveDailySteps(stepCount, day)
    }
}