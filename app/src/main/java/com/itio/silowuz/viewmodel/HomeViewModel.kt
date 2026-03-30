package com.itio.silowuz.viewmodel

import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.BarEntry
import com.itio.silowuz.model.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

class HomeViewModel  : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState(
        userName = getUserName(),
        dateString = getCurrentDate(),
        steps = getCurrentSteps(),
        stepGoal = getStepGoal(),
        calories = calculateCaloriesBurned(getCurrentSteps()),
        distanceKm = calculateTraversedDistanceKm(getCurrentSteps()),
        streakDays = getActivityStreak(),
        activeMinutes = getActivityMinutes(),
        isTracking = getIsTracking(),
        barEntries = getWeeklySteps()
    ))
    val uiState = _uiState.asStateFlow()

    fun addSteps() {
        _uiState.update {
            val newSteps = it.steps + 100
            it.copy(steps = newSteps,
                calories = calculateCaloriesBurned(newSteps),
                distanceKm = calculateTraversedDistanceKm(newSteps))
        }
    }

    fun toggleTracking() {
        _uiState.update { it.copy(isTracking = !it.isTracking) }
    }

    fun getUserName() : String {
        return "Użytkownik"
    }

    fun getCurrentDate() : String{
        val locale = Locale("pl", "PL")
        val formater = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", locale)

        return LocalDate.now().format(formater)
    }

    fun getCurrentSteps() : Int{
        return 0
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

    fun getIsTracking() : Boolean{
        return true
    }

    fun getWeeklySteps(): List<BarEntry>{
        val barEntriesList = ArrayList<BarEntry>()
        barEntriesList.add(BarEntry(0f, 100f))
        barEntriesList.add(BarEntry(1f, 200f))
        barEntriesList.add(BarEntry(2f, 300f))
        barEntriesList.add(BarEntry(3f, 400f))
        barEntriesList.add(BarEntry(4f, 500f))
        barEntriesList.add(BarEntry(5f, 600f))
        barEntriesList.add(BarEntry(6f, 700f))
        return barEntriesList
    }
}