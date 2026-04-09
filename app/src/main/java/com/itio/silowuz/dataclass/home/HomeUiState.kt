package com.itio.silowuz.dataclass.home

import com.github.mikephil.charting.data.BarEntry

data class HomeUiState (
    val userName: String = "",
    val dateString: String = "",
    val steps: Int = 0,
    val stepGoal: Int = 10000,
    val calories: Int = 0,
    val distanceKm: Double = 0.0,
    val streakDays: Int = 0,
    val activeMinutes: Int = 0,
    val isTracking: Boolean = false,
    val barEntries: List<BarEntry> = emptyList()
)