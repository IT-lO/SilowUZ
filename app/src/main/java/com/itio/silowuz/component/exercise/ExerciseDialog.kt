package com.itio.silowuz.component.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.itio.silowuz.R
import com.itio.silowuz.dataclass.exercise.Exercise
import com.itio.silowuz.ui.theme.MainGreen
import com.itio.silowuz.ui.theme.White

@Composable
fun ExerciseDialog(
    exercise: Exercise? = null,
    onDismissRequest: () -> Unit,
    onSave: (String, Int, Int, Double, Int) -> Unit,
    paddingValues: PaddingValues,
) {
    var name by remember { mutableStateOf(exercise?.name ?: "") }
    var defaultReps by remember { mutableStateOf(exercise?.reps?.toString() ?: "") }
    var defaultSets by remember { mutableStateOf(exercise?.sets?.toString() ?: "") }
    var defaultWeight by remember { mutableStateOf(exercise?.weight?.toString() ?: "") }
    var defaultDuration by remember { mutableStateOf(exercise?.duration?.toString() ?: "") }

    val dashedEffect = PathEffect.dashPathEffect(
        intervals = floatArrayOf(20f, 20f),
        phase = 0f
    )

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = White,
                    shape = RoundedCornerShape(15.dp)
                )
                .drawBehind {
                    drawRoundRect(
                        color = MainGreen,
                        style = Stroke(
                            width = 5.dp.toPx(),
                            pathEffect = dashedEffect
                        ),
                        cornerRadius = CornerRadius(15.dp.toPx())
                    )
                }
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.exercise_name),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = stringResource(R.string.exercise_name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = defaultReps,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) {
                            defaultReps = newValue
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    label = { Text(text = stringResource(R.string.exercise_reps)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = defaultSets,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) {
                            defaultSets = newValue
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    label = { Text(text = stringResource(R.string.exercise_sets)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = defaultWeight,
                    onValueChange = { input ->
                        val normalizedInput = input.replace(',', '.')

                        val isDouble = normalizedInput.count { it == '.' } <= 1 &&
                                normalizedInput.all { it.isDigit() || it == '.' }

                        if (isDouble) {
                            if (normalizedInput.contains(".")) {
                                val afterDot = normalizedInput.substringAfter(".")
                                if (afterDot.length <= 2) {
                                    defaultWeight = normalizedInput
                                }
                            } else {
                                defaultWeight = normalizedInput
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(text = stringResource(R.string.exercise_weight)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = defaultDuration,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) {
                            defaultDuration = newValue
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(text = stringResource(R.string.exercise_duration)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onDismissRequest) {
                    Text(text = stringResource(R.string.cancel))
                }
                Button(
                    onClick = {
                        val nameValue = name
                        val repsValue = defaultReps.toIntOrNull() ?: 0
                        val setsValue = defaultSets.toIntOrNull() ?: 0
                        val weightValue = defaultWeight.toDoubleOrNull() ?: 0.0
                        val durationValue = defaultDuration.toIntOrNull() ?: 0
                        onSave( nameValue, repsValue, setsValue, weightValue, durationValue )
                    }
                ) {
                    Text(text = stringResource(R.string.save))
                }
            }
        }
    }
}