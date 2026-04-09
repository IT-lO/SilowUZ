package com.itio.silowuz.dataclass.exercise

data class TrainingPlan(
    val id: String = "",
    val name: String = "",
    val exerciseIds: List<String> = emptyList(),
    val userId: String = ""
)
