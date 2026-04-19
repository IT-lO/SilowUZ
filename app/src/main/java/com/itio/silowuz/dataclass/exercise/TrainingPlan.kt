package com.itio.silowuz.dataclass.exercise

import kotlinx.serialization.Serializable

@Serializable
data class TrainingPlan(
    val id: String = "",
    val name: String = "",
    val exerciseIds: List<String> = emptyList(),
    val userId: String = ""
)
