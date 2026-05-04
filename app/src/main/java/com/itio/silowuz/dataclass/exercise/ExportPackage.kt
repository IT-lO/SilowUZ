package com.itio.silowuz.dataclass.exercise
import kotlinx.serialization.Serializable

/**
 * Data class representing a fully packaged training plan for export or sharing.
 * Bundles the core training plan details with its associated exercises into a single,
 * serializable object, making it easy to transfer data (e.g., via Bluetooth or JSON).
 *
 * @param planName The display name of the training plan being exported
 * @param trainingPlan The main [TrainingPlan] object containing plan-specific details
 * @param associatedExercises A list of [Exercise] objects that belong to this training plan
 */
@Serializable
data class ExportPackage(
    val planName: String,
    val trainingPlan: TrainingPlan,
    val associatedExercises: List<Exercise>
)