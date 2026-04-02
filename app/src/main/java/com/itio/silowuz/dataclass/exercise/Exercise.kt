package com.itio.silowuz.dataclass.exercise

data class Exercise(
//    val id: String = UUID.randomUUID().toString(), // TODO setup for firebase
    val name: String,
    val reps: Int,
    val sets: Int,
    val weight: Double?,
    val duration: Int?
)