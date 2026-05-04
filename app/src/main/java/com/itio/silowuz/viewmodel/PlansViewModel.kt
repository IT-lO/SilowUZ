package com.itio.silowuz.viewmodel

import android.bluetooth.BluetoothAdapter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.itio.silowuz.component.exercise.BluetoothSocket
import com.itio.silowuz.dataclass.exercise.Exercise
import com.itio.silowuz.dataclass.exercise.ExportPackage
import com.itio.silowuz.dataclass.exercise.TrainingPlan
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * ViewModel for managing training plans and exercises.
 * Handles fetching, saving, updating, deleting exercises and plans from Firebase,
 * as well as Bluetooth import/export functionality.
 */
class PlansViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()
    private val userId get() = auth.currentUser?.uid ?: ""

    var exercises by mutableStateOf<List<Exercise>>(emptyList())
    var plans by mutableStateOf<List<TrainingPlan>>(emptyList())

    init {
        fetchData()
    }

    /**
     * Fetches user's exercises and training plans from Firebase Firestore.
     * Sets up real-time listeners to automatically update data when changes occur.
     */
    fun fetchData() {
        if (userId.isEmpty()) return

        db.collection("exercises")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, _ ->
                exercises = snapshot?.toObjects(Exercise::class.java) ?: emptyList()
            }

        db.collection("plans")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, _ ->
                plans = snapshot?.toObjects(TrainingPlan::class.java) ?: emptyList()
            }
    }

    /**
     * Saves an exercise to Firebase Firestore.
     * If the exercise has no ID, creates a new document; otherwise updates the existing one.
     * Automatically adds the current user's ID to the exercise.
     * 
     * @param exercise The exercise object to save
     */
    fun saveExercise(exercise: Exercise) {
        val docRef = if (exercise.id.isEmpty()) {
            db.collection("exercises").document()
        } else {
            db.collection("exercises").document(exercise.id)
        }
        val finalExercise = exercise.copy(id = docRef.id, userId = userId)
        docRef.set(finalExercise)
    }

    /**
     * Creates a new training plan in Firebase Firestore.
     * The plan is created with the current user's ID and an empty document ID (assigned by Firebase).
     * 
     * @param name The name of the training plan
     * @param selectedExerciseIds List of exercise IDs included in this plan
     */
    fun savePlan(name: String, selectedExerciseIds: List<String>) {
        val docRef = db.collection("plans").document()
        val plan = TrainingPlan(id = docRef.id, name = name, exerciseIds = selectedExerciseIds, userId = userId)
        docRef.set(plan)
    }

    /**
     * Deletes an exercise from Firebase Firestore by its document ID.
     * 
     * @param id The unique identifier of the exercise to delete
     */
    fun deleteExercise(id: String) {
        db.collection("exercises").document(id).delete()
    }

    /**
     * Deletes a training plan from Firebase Firestore by its document ID.
     * Does nothing if the provided plan ID is empty.
     * 
     * @param planId The unique identifier of the plan to delete
     */
    fun deletePlan(planId: String) {
        if (planId.isEmpty()) return

        db.collection("plans")
            .document(planId)
            .delete()
            .addOnSuccessListener {}
            .addOnFailureListener { e -> print(e.message)}
    }

    /**
     * Updates an existing training plan in Firebase Firestore.
     * Ensures the plan contains the current user's ID before updating.
     * 
     * @param plan The updated TrainingPlan object to save
     */
    fun updatePlan(plan: TrainingPlan) {
        val planWithUserId = plan.copy(userId = userId)
        db.collection("plans").document(plan.id).set(planWithUserId)

    }

    /**
     * Serializes a training plan and its associated exercises into a JSON string for Bluetooth export.
     * Filters exercises that are part of the plan and packages them with the plan data.
     * 
     * @param plan The training plan to export
     * @return JSON-encoded string containing the export package
     */
    fun exportPlan(plan: TrainingPlan): String {
        val associated = exercises.filter { plan.exerciseIds.contains(it.id) }
        val pkg = ExportPackage(plan.name, plan, associated)
        return Json.encodeToString(pkg)
    }

    /**
     * Starts a Bluetooth server and exports the specified training plan to a connected device.
     * Serializes the plan and its exercises to JSON before sending via Bluetooth.
     * 
     * @param adapter The BluetoothAdapter to use for communication
     * @param plan The training plan to export
     * @param onComplete Callback invoked with true if export succeeded, false otherwise
     */
    fun startBluetoothExport(adapter: BluetoothAdapter, plan: TrainingPlan, onComplete: (Boolean) -> Unit) {
        val json = exportPlan(plan)
        viewModelScope.launch {
            val result = BluetoothSocket.startServerAndSend(adapter, json)
            onComplete(result.isSuccess)
        }
    }

    /**
     * Imports a training plan and its exercises from a connected Bluetooth device.
     * Receives JSON data, parses it, and saves the associated exercises and plan to Firebase.
     * 
     * @param device The BluetoothDevice to connect to for importing
     * @param onComplete Callback invoked with true if import succeeded, false otherwise
     */
    fun importPlanFromDevice(device: android.bluetooth.BluetoothDevice, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = BluetoothSocket.connectAndReceive(device)

            result.onSuccess { json ->
                try {
                    val pkg = Json.decodeFromString<ExportPackage>(json)

                    pkg.associatedExercises.forEach { exercise ->
                        saveExercise(exercise)
                    }

                    savePlan(pkg.trainingPlan.name, pkg.trainingPlan.exerciseIds)

                    onComplete(true)
                } catch (e: Exception) {
                    onComplete(false)
                }
            }.onFailure {
                onComplete(false)
            }
        }
    }

    /**
     * Retrieves exercises that belong to a specific training plan by matching their IDs.
     * Filters the stored exercises list based on the provided exercise IDs.
     * 
     * @param exerciseIds List of exercise IDs that should be included in this plan
     * @return A filtered list of Exercise objects belonging to the plan
     */
    fun getExercisesForPlan(exerciseIds: List<String>): List<Exercise> {
        return exercises.filter { it.id in exerciseIds }
    }
}
