package com.itio.silowuz.dataclass.exercise

data class Exercise(
    val id: String = "",
    val name: String = "",
    val reps: Int = 0,
    val sets: Int = 0,
    val weight: Double? = null,
    val duration: Int? = null,
    val userId: String = ""
)