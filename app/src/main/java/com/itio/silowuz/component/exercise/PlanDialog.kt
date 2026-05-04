package com.itio.silowuz.component.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.itio.silowuz.dataclass.exercise.Exercise
import com.itio.silowuz.dataclass.exercise.TrainingPlan
import com.itio.silowuz.R

@Composable
fun PlanDialog(
    planToEdit: TrainingPlan? = null,
    availableExercises: List<Exercise>,
    onDismissRequest: () -> Unit,
    onSave: (String, List<String>) -> Unit
) {
    var planName by remember { mutableStateOf(planToEdit?.name ?: "") }
    val selectedIds = remember {
        mutableStateListOf<String>().apply {
            planToEdit?.exerciseIds?.let { addAll(it) }
        }
    }

    var expanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(15.dp))
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = if (planToEdit == null) stringResource(R.string.new_plan) else stringResource(R.string.edit_plan),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = planName,
                onValueChange = { planName = it },
                label = { Text(stringResource(R.string.plan_name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Column {
                Text(stringResource(R.string.add_exercise_to_plan), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))

                Box {
                    OutlinedTextField(
                        value = stringResource(R.string.choose_exercise),
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.clickable { expanded = true }
                            )
                        }
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { expanded = true }
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth(0.7f)
                    ) {
                        val filteredExercises = availableExercises.filter { exercise ->
                            !selectedIds.contains(exercise.id)
                        }

                        if (filteredExercises.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.no_more_exercises), color = MaterialTheme.colorScheme.onSurfaceVariant) },
                                onClick = { expanded = false }
                            )
                        } else {
                            filteredExercises.forEach { exercise ->
                                DropdownMenuItem(
                                    text = {
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Text(exercise.name, fontWeight = FontWeight.Medium)
                                            Text("${exercise.reps}x${exercise.sets}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    },
                                    onClick = {
                                        selectedIds.add(exercise.id)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 250.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(selectedIds) { id ->
                    val exercise = availableExercises.find { it.id == id }
                    if (exercise != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(exercise.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("${exercise.reps} x ${exercise.sets}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            IconButton(onClick = { selectedIds.remove(id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.delete),
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(stringResource(R.string.cancel), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Button(

                    onClick = { onSave(planName, selectedIds.toList()) },
                    modifier = Modifier.weight(1f),
                    enabled = planName.isNotBlank() && selectedIds.isNotEmpty(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(stringResource(R.string.save))
                }
            }
        }
    }
}