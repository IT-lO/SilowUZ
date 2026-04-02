package com.itio.silowuz.component.exercise

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.itio.silowuz.R
import com.itio.silowuz.ui.theme.MainGreen

@Composable
fun ExerciseDialog(
    onDismissRequest: () -> Unit,
    onSave: (Int) -> Unit,
    paddingValues: PaddingValues
){
    var name by remember { mutableStateOf("") }
    var defaultReps by remember { mutableStateOf("") }
    var defaultSets by remember { mutableStateOf("") }
    var defaultWeight by remember { mutableStateOf("") }
    var defaultDuration by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = {onDismissRequest},
    ) {
        Surface(
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .dashedBorder(
                        color = MainGreen,
                        strokeWidth = 2.dp,
                        cornerRadius = 15.dp,
                        dashLength = 10.dp,
                        gapLength = 10.dp
                    )
                    .padding(16.dp),
            ) {
                Text(text = stringResource(R.string.exercise_info_name))
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(text = stringResource(R.string.exercise_label_name), color = MaterialTheme.colorScheme.onSurface) },
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row() {
                    Text(text = stringResource(R.string.exercise_info_reps))
                    TextField(
                        value = defaultReps,
                        onValueChange = { defaultReps = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        label = { Text(text = stringResource(R.string.exercise_label_reps), color = MaterialTheme.colorScheme.onSurface) },
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = stringResource(R.string.exercise_info_sets))
                    TextField(
                        value = defaultSets,
                        onValueChange = { defaultSets = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        label = { Text(text = stringResource(R.string.exercise_label_sets), color = MaterialTheme.colorScheme.onSurface) },
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Row() {
                    Column() {
                        Text(text = stringResource(R.string.exercise_info_weight))
                        TextField(
                            value = defaultWeight,
                            onValueChange = { defaultWeight = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            label = { Text(text = stringResource(R.string.exercise_label_weight), color = MaterialTheme.colorScheme.onSurface) },
                            modifier = Modifier
                                .padding(8.dp)
                                .weight(1f),
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column() {
                        Text(text = stringResource(R.string.exercise_info_duration))
                        TextField(
                            value = defaultDuration,
                            onValueChange = { defaultDuration = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            label = { Text(text = stringResource(R.string.exercise_label_duration), color = MaterialTheme.colorScheme.onSurface) },
                            modifier = Modifier
                                .padding(8.dp)
                                .weight(1f),
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = {onDismissRequest}
                    ) {
                        Text(text = stringResource(R.string.info_cancel))
                    }
                    Button(
                        onClick = {
                            val repsValue = defaultReps.toIntOrNull() ?: 0
                            onSave(repsValue)
                        },
                    ) {
                        Text(text = stringResource(R.string.info_save))
                    }
                }
            }
        }
    }

}


fun Modifier.dashedBorder(
    color: Color,
    strokeWidth: Dp,
    cornerRadius: Dp,
    dashLength: Dp,
    gapLength: Dp
) = this.drawBehind {
    val dashedPathEffect = PathEffect.dashPathEffect(
        intervals = floatArrayOf(dashLength.toPx(), gapLength.toPx()),
        phase = 0f
    )

    drawRoundRect(
        color = color,
        style = Stroke(
            width = strokeWidth.toPx(),
            pathEffect = dashedPathEffect
        ),
        cornerRadius = CornerRadius(cornerRadius.toPx())
    )
}