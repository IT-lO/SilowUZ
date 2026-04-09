package com.itio.silowuz.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.itio.silowuz.dataclass.exercise.Exercise
import com.itio.silowuz.dataclass.exercise.TrainingPlan
class PlansViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()
    private val userId get() = auth.currentUser?.uid ?: ""

    var exercises by mutableStateOf<List<Exercise>>(emptyList())
    var plans by mutableStateOf<List<TrainingPlan>>(emptyList())

    init {
        // Włączenie bazy lokalnej (Firestore robi to domyślnie na Androidzie)
        fetchData()
    }

    private fun fetchData() {
        if (userId.isEmpty()) return

        // Słuchanie zmian w czasie rzeczywistym (działa offline!)
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
}