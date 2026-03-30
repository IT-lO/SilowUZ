package com.itio.silowuz.component.exercise

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.itio.silowuz.screen.Exercise

@Composable
fun ExerciseCard(
    exercise: Exercise,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(16.dp, 4.dp)),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = exercise.name)
                Text(text = exercise.reps.toString())
                Text(text = exercise.series.toString())
                Text(text = exercise.weight.toString())
                Text(text = exercise.duration.toString())
            }
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .width(IntrinsicSize.Max),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TextButton(
                    onClick = onEdit,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(text = "Edit")
                }
                TextButton(
                    onClick = onDelete,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(text = "Delete")
                }
            }
        }
    }
}