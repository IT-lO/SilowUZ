package com.itio.silowuz.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly
import com.itio.silowuz.R
import kotlin.time.Duration

data class Exercise(
    val name: String
)

data class ExerciseSeries(
    val reps: Int,
    val series: Int,
    val weight: Double,
//    val duration: Duration?,
//    val distance: Double?,
//    val heartRate: Int?
)

@Composable
fun ExerciseScreen(paddingValues: PaddingValues){

    var showExerciseDialog by remember { mutableStateOf(false) }
    var seriesList by remember { mutableStateOf(listOf<ExerciseSeries>()) }
    var reps by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Scaffold(
            floatingActionButton = {

                FloatingActionButton(
                    onClick = { showExerciseDialog = true },
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Exercise")
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
            ){
                items(seriesList){ series ->
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(PaddingValues(16.dp, 4.dp))
                            .align(Alignment.CenterHorizontally),
                        border = BorderStroke(1.dp, Color.Black)
                    ) {
                        Text(
                            text = series.reps.toString(),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = "sss",
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = "sss",
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }

        if(showExerciseDialog){
            Dialog(onDismissRequest = {showExerciseDialog = false}){
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    tonalElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        Text(text = "Informations about series")
                        TextField(
                            value = reps,
                            onValueChange = { reps = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            label = { Text("Reps", color = MaterialTheme.colorScheme.onSurface) },
                            modifier = Modifier.padding(8.dp),

                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(onClick = {showExerciseDialog = false}) {
                                Text(text = "Cancel")
                            }
                            Button(onClick = {
                                seriesList = seriesList + ExerciseSeries(Integer.getInteger(reps, 0),3,99.9)
                                showExerciseDialog = false
                            }) {
                                Text(text = "Save")
                            }

                        }

                    }
                }
            }
        }
    }

}