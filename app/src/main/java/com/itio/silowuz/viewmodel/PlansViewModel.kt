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
class PlansViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()
    private val userId get() = auth.currentUser?.uid ?: ""

    var exercises by mutableStateOf<List<Exercise>>(emptyList())
    var plans by mutableStateOf<List<TrainingPlan>>(emptyList())

    init {
        fetchData()
    }

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

    fun saveExercise(exercise: Exercise) {
        val docRef = if (exercise.id.isEmpty()) {
            db.collection("exercises").document()
        } else {
            db.collection("exercises").document(exercise.id)
        }
        val finalExercise = exercise.copy(id = docRef.id, userId = userId)
        docRef.set(finalExercise)
    }

    fun savePlan(name: String, selectedExerciseIds: List<String>) {
        val docRef = db.collection("plans").document()
        val plan = TrainingPlan(id = docRef.id, name = name, exerciseIds = selectedExerciseIds, userId = userId)
        docRef.set(plan)
    }

    fun deleteExercise(id: String) {
        db.collection("exercises").document(id).delete()
    }

    fun deletePlan(planId: String) {
        if (planId.isEmpty()) return

        db.collection("plans")
            .document(planId)
            .delete()
            .addOnSuccessListener {}
            .addOnFailureListener { e -> print(e.message)}
    }

    fun updatePlan(plan: TrainingPlan) {
        val planWithUserId = plan.copy(userId = userId)
        db.collection("plans").document(plan.id).set(planWithUserId)

    }

    fun exportPlan(plan: TrainingPlan): String {
        val associated = exercises.filter { plan.exerciseIds.contains(it.id) }
        val pkg = ExportPackage(plan.name, plan, associated)
        return Json.encodeToString(pkg)
    }

    fun startBluetoothExport(adapter: BluetoothAdapter, plan: TrainingPlan, onComplete: (Boolean) -> Unit) {
        val json = exportPlan(plan)
        viewModelScope.launch {
            val result = BluetoothSocket.startServerAndSend(adapter, json)
            onComplete(result.isSuccess)
        }
    }

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

    fun getExercisesForPlan(exerciseIds: List<String>): List<Exercise> {
        return exercises.filter { it.id in exerciseIds }
    }
}