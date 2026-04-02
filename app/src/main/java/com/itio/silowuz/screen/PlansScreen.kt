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
import com.itio.silowuz.R
import com.itio.silowuz.component.exercise.ExerciseCard
import com.itio.silowuz.component.exercise.ModeSwitch
import com.itio.silowuz.component.exercise.PlanCard
import com.itio.silowuz.component.exercise.ExerciseDialog
import com.itio.silowuz.dataclass.exercise.Exercise
import com.itio.silowuz.dataclass.exercise.TrainingPlan
import com.itio.silowuz.`interface`.IconResource

@Composable
fun ExerciseScreen(paddingValues: PaddingValues){

    var showExerciseDialog by remember { mutableStateOf(false) }
    var exerciseList by remember { mutableStateOf(listOf<Exercise>()) }

    var showMenu by remember { mutableStateOf(false) }
    val iconRotation by animateFloatAsState(targetValue = if (showMenu) 45f else 0f)

    val addExerciseIcon : IconResource = IconResource.Drawable(R.drawable.add_exercise_ico)
    val addSeriesIcon : IconResource = IconResource.Drawable(R.drawable.add_series_ico)

    var plansMode by remember { mutableStateOf(true) }
    var plan = TrainingPlan(name = "Plan", exerciseList = exerciseList)


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
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 80.dp)
        ){
            if (!plansMode) {
                items(exerciseList) { exercise ->
                    ExerciseCard(
                        exercise = exercise,
                        onEdit = { },
                        onDelete = { exerciseList = exerciseList - exercise }
                    )
                }
            } else {
                if(!exerciseList.isEmpty()){
                    item {
                        PlanCard(plan)
                    }
                }
            }
        }
    }

    if(showExerciseDialog){
        ExerciseDialog(
            onDismissRequest = {
                showExerciseDialog = false
            },
            onSave = { repsValue ->
                exerciseList = exerciseList + Exercise("Biceps Curl", repsValue, 3, 99.9, 0)
                showExerciseDialog = false
            },
            paddingValues = paddingValues
        )
    }
}
