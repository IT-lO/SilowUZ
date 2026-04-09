package com.itio.silowuz.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.itio.silowuz.R
import com.itio.silowuz.component.exercise.ExerciseCard
import com.itio.silowuz.component.exercise.ModeSwitch
import com.itio.silowuz.component.exercise.PlanCard
import com.itio.silowuz.component.exercise.ExerciseDialog
import com.itio.silowuz.component.exercise.PlanDialog
import com.itio.silowuz.dataclass.exercise.Exercise
import com.itio.silowuz.dataclass.exercise.TrainingPlan
import com.itio.silowuz.`interface`.IconResource
import com.itio.silowuz.viewmodel.PlansViewModel

@Composable
fun PlansScreen(
    paddingValues: PaddingValues,
    viewModel: PlansViewModel = viewModel()
){

    var showExerciseDialog by remember { mutableStateOf(false) }
    var showPlanDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var plansMode by remember { mutableStateOf(true) }
    var exerciseToEdit by remember { mutableStateOf<Exercise?>(null) }

    val addExerciseIcon : IconResource = IconResource.Drawable(R.drawable.add_exercise_ico)
    val addSeriesIcon : IconResource = IconResource.Drawable(R.drawable.add_series_ico)
    val iconRotation by animateFloatAsState(targetValue = if (showMenu) 45f else 0f)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .padding(8.dp,16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                ModeSwitch(
                    plansMode = plansMode,
                    onChangeMode = { newMode -> plansMode = newMode }
                )
            }
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End
            ) {

                AnimatedVisibility(
                    visible = showMenu,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 } ),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 } )
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .padding(bottom = 16.dp)


                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = stringResource(R.string.create_exercise))
                            Spacer(modifier = Modifier.width(8.dp))
                            SmallFloatingActionButton(
                                onClick = { showExerciseDialog = true},
                            ) {
                                when(addExerciseIcon){
                                    is IconResource.Drawable -> {
                                        Icon(
                                            painter = painterResource(id = addExerciseIcon.resId),
                                            contentDescription = stringResource(R.string.create_exercise)
                                        )
                                    }
                                    else -> { }
                                }
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = stringResource(R.string.create_plan))
                            Spacer(modifier = Modifier.width(8.dp))
                            SmallFloatingActionButton(
                                onClick = {},
                            ) {
                                when(addSeriesIcon){
                                    is IconResource.Drawable -> {
                                        Icon(
                                            painter = painterResource(id = addSeriesIcon.resId),
                                            contentDescription = stringResource(R.string.create_plan)
                                        )
                                    }
                                    else -> { }
                                }
                            }
                        }
                    }
                }
                FloatingActionButton(
                    onClick = { showMenu = !showMenu },
                ) {
                    Icon(Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.exercise_menu),
                        modifier = Modifier.rotate(iconRotation)
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            if (!plansMode) {
                items(viewModel.exercises) { exercise ->
                    ExerciseCard(
                        exercise = exercise,
                        onEdit = { exerciseToEdit = exercise },
                        onDelete = { viewModel.deleteExercise(exercise.id) }
                    )
                }
            } else {
                items(viewModel.plans) { plan ->
                    // PlanCard musi teraz przyjąć listę IDs lub ViewModel musi mu dostarczyć pełne obiekty
                    PlanCard(plan)
                }
            }
        }
    }

    // Dialog Ćwiczenia
    if (showExerciseDialog || exerciseToEdit != null) {
        ExerciseDialog(
            exercise = exerciseToEdit,
            onDismissRequest = {
                showExerciseDialog = false
                exerciseToEdit = null
            },
            onSave = { name, reps, sets, weight, duration ->
                viewModel.saveExercise(Exercise(exerciseToEdit?.id ?: "", name, reps, sets, weight, duration))
                showExerciseDialog = false
                exerciseToEdit = null
            },
            paddingValues = paddingValues
        )
    }

    // Dialog Planu
    if (showPlanDialog) {
        PlanDialog(
            availableExercises = viewModel.exercises,
            onDismissRequest = { showPlanDialog = false },
            onSave = { name, selectedIds ->
                viewModel.savePlan(name, selectedIds)
                showPlanDialog = false
            }
        )
    }
}
