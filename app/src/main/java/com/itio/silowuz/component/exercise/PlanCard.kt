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
import com.itio.silowuz.R
import com.itio.silowuz.dataclass.exercise.TrainingPlan
import com.itio.silowuz.ui.theme.MainGreen
import com.itio.silowuz.ui.theme.SecondaryGreen
import com.itio.silowuz.ui.theme.White

@Composable
fun PlanCard(
    trainingPlan: TrainingPlan,
    // TODO dodać delegate funckję do usuwania planu
) {
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

        trainingPlan.exerciseList.forEach { exercise ->
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
                    text = "${exercise.series}x${exercise.reps}"
                )
            }
            HorizontalDivider(color = SecondaryGreen)
            Spacer(modifier = Modifier.height(8.dp))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        enabled = true,
                        onClick = {}
                    )
                    .clip(shape = RoundedCornerShape(7.dp))
                    .background(color = MainGreen)
                    .height(height = 48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.start_training), color = White)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        enabled = true,
                        onClick = {}
                    )
                    .clip(shape = RoundedCornerShape(7.dp))
                    .background(color = MainGreen)
                    .height(height = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "B", color = White)
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        enabled = true,
                        onClick = {}
                    )
                    .clip(shape = RoundedCornerShape(7.dp))
                    .background(color = MainGreen)
                    .height(height = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "E", color = White)
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        enabled = true,
                        onClick = {}
                    )
                    .clip(shape = RoundedCornerShape(7.dp))
                    .background(color = MainGreen)
                    .height(height = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "U", color = White)
            }
        }
    }
}