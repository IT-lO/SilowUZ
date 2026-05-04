package com.itio.silowuz.component.exercise

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itio.silowuz.R
import com.itio.silowuz.dataclass.exercise.Exercise
import com.itio.silowuz.dataclass.exercise.TrainingPlan
import com.itio.silowuz.ui.theme.MainGreen
import com.itio.silowuz.ui.theme.SecondaryGreen
import com.itio.silowuz.ui.theme.White

/**
 * Composable card displaying a training plan with its exercises and action buttons.
 * Shows the plan name, list of exercises with their reps/sets, and provides quick actions
 * for starting training, exporting, editing, or deleting the plan.
 * 
 * @param trainingPlan The training plan to display
 * @param allExercises List of all available exercises to filter those in this plan
 * @param onStartTraining Callback invoked when user clicks the "Start Training" button
 * @param onEdit Callback invoked when user clicks the Edit (E) button
 * @param onDelete Callback invoked when user clicks the Delete (U) button
 * @param onExport Callback invoked when user clicks the Export (B) button
 */
@Composable
fun PlanCard(
    trainingPlan: TrainingPlan,
    allExercises: List<Exercise>,
    onStartTraining: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onExport: () -> Unit
) {
    val exercisesInPlan = trainingPlan.exerciseIds.mapNotNull { id ->
        allExercises.find { it.id == id }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(15.dp))
            .border(
                border = BorderStroke(2.dp, SecondaryGreen),
                shape = RoundedCornerShape(15.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = trainingPlan.name,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))

        exercisesInPlan.forEach { exercise ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = exercise.name,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${exercise.sets}x${exercise.reps}"
                )
            }
            HorizontalDivider(color = SecondaryGreen)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(shape = RoundedCornerShape(7.dp))
                    .background(color = MainGreen)
                    .clickable { onStartTraining() }
                    .height(height = 48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.start_training), color = White)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ActionButton(text = "B", modifier = Modifier.weight(1f)) { onExport() }
            ActionButton(text = "E", modifier = Modifier.weight(1f)) { onEdit() }
            ActionButton(text = "U", modifier = Modifier.weight(1f)) { onDelete() }
        }
    }
}

/**
 * Small green action button used within PlanCard for quick operations.
 * Displays a single character label (B, E, U) with click capability.
 * 
 * @param text Single character label to display on the button
 * @param modifier Modifier that can be used to customize size and layout
 * @param onClick Callback invoked when the button is clicked
 */
@Composable
fun ActionButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(7.dp))
            .background(color = MainGreen)
            .clickable { onClick() }
            .height(height = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = White, fontSize = 12.sp)
    }
}
