package com.itio.silowuz.dataclass.exercise
import kotlinx.serialization.Serializable

@Serializable
data class ExportPackage(
    val planName: String,
    val trainingPlan: TrainingPlan,
    val associatedExercises: List<Exercise>
)