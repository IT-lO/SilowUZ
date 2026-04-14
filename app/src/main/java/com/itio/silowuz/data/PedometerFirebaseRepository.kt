package com.itio.silowuz.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.itio.silowuz.dataclass.home.DailySteps
import java.time.LocalDate
import java.time.temporal.ChronoField

/*
    Repository used to save daily steps to Firebase and retrieve weekly steps.
 */
class PedometerFirebaseRepository {
    private val db = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()
    private val userId get() = auth.currentUser?.uid ?: ""

    var weeklySteps by mutableStateOf<List<DailySteps>>(emptyList())

    init {
        fetchWeeklySteps()
    }

    /*
        Fetches the steps from the current week of the logged-in user.
     */
    private fun fetchWeeklySteps() {
        if (userId.isEmpty()) return

        val now = LocalDate.now()
        val monday = now.with(ChronoField.DAY_OF_WEEK, 1).toString()
        val sunday = now.with(ChronoField.DAY_OF_WEEK, 7).toString()

        db.collection("dailySteps")
            .whereGreaterThanOrEqualTo("day", monday)
            .whereLessThanOrEqualTo("day", sunday)
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, _ ->
                weeklySteps = snapshot?.toObjects(DailySteps::class.java) ?: emptyList()
            }
    }

    /*
        Saves the daily steps to Firebase.
     */
    fun saveDailySteps(stepCount: Int, day: String) {
        if (userId.isEmpty()) return

        val docId = "${userId}_${day}"
        val docRef = db.collection("dailySteps").document(docId)
        val stepEntry = DailySteps(
            id = docId,
            userId = userId,
            stepCount = stepCount,
            day = day)
        docRef.set(stepEntry)
    }
}