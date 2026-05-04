package com.itio.silowuz.screen

import android.content.Context
import android.hardware.Sensor
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.itio.silowuz.component.exercise.ActionButton
import com.itio.silowuz.dataclass.exercise.Exercise
import com.itio.silowuz.dataclass.exercise.TrainingPlan
import com.itio.silowuz.ui.theme.MainGreen
import com.itio.silowuz.ui.theme.SecondaryGreen
import com.itio.silowuz.viewmodel.PlansViewModel
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.itio.silowuz.R
import com.itio.silowuz.ui.theme.White
import kotlinx.coroutines.delay
import kotlin.math.sqrt

/**
 * Composable function that displays the training screen where users can perform exercises.
 * This screen tracks exercise progress, handles shake detection for set completion,
 * and manages transitions between sets and exercises using Bluetooth sensor data.
 * 
 * @param plan The training plan to execute
 * @param viewModel The PlansViewModel instance for data management (uses default.viewModel() if not provided)
 * @param onFinish Callback function called when the training is completed or exited
 */
@Composable
fun TrainingScreen(
    plan: TrainingPlan,
    viewModel: PlansViewModel = viewModel(),
    onFinish: () -> Unit)
{
    BackHandler {
        onFinish()
    }

    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    var exercises: List<Exercise> by remember {mutableStateOf(viewModel.getExercisesForPlan(plan.exerciseIds))}
    var currentExerciseIndex by remember { mutableStateOf(0) }
    var currentSet by remember { mutableStateOf(1) }
    var showSuccessPopup by remember { mutableStateOf(false) }
    var restTimeLeft by remember { mutableStateOf(0) }
    val currentExercise = exercises.getOrNull(currentExerciseIndex)

    var shakeCount by remember { mutableStateOf(0) }
    var nextClickCount by remember { mutableStateOf(0) }
    var prevClickCount by remember { mutableStateOf(0) }
    var confirmationMessage by remember { mutableStateOf("") }

    val shakeAgainText = stringResource(R.string.shake_again)
    val shakeToFinishText = stringResource(R.string.shake_to_finish_set)
    val nextText = stringResource(R.string.next)
    val previousText = stringResource(R.string.previous)
    val confirmText = stringResource(R.string.confirm)

    LaunchedEffect(shakeCount, nextClickCount, prevClickCount) {
        if (shakeCount > 0 || nextClickCount > 0 || prevClickCount > 0) {
            delay(3000)
            shakeCount = 0
            nextClickCount = 0
            prevClickCount = 0
            confirmationMessage = ""
        }
    }

    LaunchedEffect(Unit) {
        val shakeDetector = ShakeDetector {
            if (!showSuccessPopup && restTimeLeft <= 0) {
                if (shakeCount == 0) {
                    shakeCount = 1
                    confirmationMessage = shakeAgainText
                } else {
                    shakeCount = 0
                    confirmationMessage = ""
                    showSuccessPopup = true
                }
            }
        }
        sensorManager.registerListener(
            shakeDetector,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_UI
        )
    }

    LaunchedEffect(showSuccessPopup) {
        if (showSuccessPopup) {
            delay(5000)
            showSuccessPopup = false
            restTimeLeft = 10
        }
    }

    LaunchedEffect(restTimeLeft) {
        if (restTimeLeft > 0) {
            delay(1000)
            restTimeLeft -= 1
            if (restTimeLeft == 0) {
                if (currentExercise != null && currentSet < currentExercise.sets) {
                    currentSet++
                } else if (currentExerciseIndex < exercises.size - 1) {
                    currentExerciseIndex++
                    currentSet = 1
                } else {
                    onFinish()
                }
            }
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = "${plan.name}", style = MaterialTheme.typography.headlineSmall)
                Text(
                    text = stringResource(R.string.exercise) + " ${currentExerciseIndex + 1} " + stringResource(R.string.of) + " ${exercises.size}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MainGreen
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (currentExercise != null) {
                    Text(
                        text = stringResource(R.string.set) + "$currentSet " + stringResource(R.string.of) + " ${currentExercise.sets}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MainGreen
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ExerciseProgressCard(exercise = currentExercise)

                    Spacer(modifier = Modifier.height(24.dp))

                    if (restTimeLeft <= 0 && !showSuccessPopup) {
                        Text(
                            text = if (confirmationMessage.isEmpty()) shakeToFinishText else confirmationMessage,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (confirmationMessage.isEmpty()) SecondaryGreen else MainGreen
                        )
                    }

                    if (restTimeLeft > 0) {
                        Text(
                            text = stringResource(R.string.short_break) + ": ${restTimeLeft}s",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MainGreen
                        )
                    }
                } else {
                    Text(stringResource(R.string.no_exercises_left))
                    ActionButton(text = stringResource(R.string.back), onClick = onFinish)
                }
            }

            if (currentExercise != null && !showSuccessPopup && restTimeLeft <= 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ActionButton(
                        text = if (prevClickCount == 0) previousText else confirmText,
                        modifier = Modifier.width(120.dp),
                        onClick = {
                            if (currentExerciseIndex > 0) {
                                if (prevClickCount == 0) {
                                    prevClickCount = 1
                                    nextClickCount = 0
                                } else {
                                    currentExerciseIndex--
                                    currentSet = 1
                                    prevClickCount = 0
                                }
                            }
                        }
                    )

                    ActionButton(
                        text = if (nextClickCount == 0) nextText else confirmText,
                        modifier = Modifier.width(120.dp),
                        onClick = {
                            if (nextClickCount == 0) {
                                nextClickCount = 1
                                prevClickCount = 0
                            } else {
                                if (currentExerciseIndex < exercises.size - 1) {
                                    currentExerciseIndex++
                                    currentSet = 1
                                } else {
                                    onFinish()
                                }
                                nextClickCount = 0
                            }
                        }
                    )
                }
            }

            if (showSuccessPopup) {
                Box(
                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MainGreen)
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(stringResource(R.string.set_finished), color = White, style = MaterialTheme.typography.headlineMedium)
                        Text(stringResource(R.string.rest_for_a_while), color = White)
                    }
                }
            }
        }
    }
}

/**
 * Displays a card showing exercise progress information including name, sets, reps, and weight.
 * The card uses a styled border and background for visual distinction.
 * 
 * @param exercise The exercise to display progress for
 */
@Composable
fun ExerciseProgressCard(exercise: Exercise) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(SecondaryGreen.copy(alpha = 0.1f))
            .border(2.dp, SecondaryGreen, RoundedCornerShape(15.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = exercise.name, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            ProgressStat(label = stringResource(R.string.exercise_sets), value = exercise.sets.toString())
            ProgressStat(label = stringResource(R.string.exercise_reps), value = exercise.reps.toString())
            if (exercise.weight != null) {
                ProgressStat(label = stringResource(R.string.exercise_weight), value = "${exercise.weight} kg")
            }
        }
    }
}

/**
 * Displays a single progress statistic with a label and value.
 * Used within ExerciseProgressCard to show individual exercise metrics.
 * 
 * @param label The text label for the statistic (e.g., "Sets", "Reps")
 * @param value The numeric value to display
 */
@Composable
fun ProgressStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Text(text = value, style = MaterialTheme.typography.titleLarge, color = MainGreen)
    }
}

/**
 * SensorEventListener that detects device shake movements.
 * Uses accelerometer data to calculate g-force and determines if the device
 * has been shaken (threshold > 2.7g). Includes cooldown period (1 second) between detections.
 * 
 * @param onShake Callback function invoked when a valid shake is detected
 */
class ShakeDetector(private val onShake: () -> Unit) : SensorEventListener {
    private var lastShakeTime: Long = 0

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val gForce = sqrt(x * x + y * y + z * z) / SensorManager.GRAVITY_EARTH

        if (gForce > 2.7f) {
            val now = System.currentTimeMillis()
            if (lastShakeTime + 1000 > now) return
            lastShakeTime = now
            onShake()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
