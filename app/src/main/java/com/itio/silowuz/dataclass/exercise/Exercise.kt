package com.itio.silowuz.dataclass.exercise

data class Exercise(
    val name: String,
    val reps: Int,
    val series: Int,
    val weight: Double?,
    val duration: Int?
)