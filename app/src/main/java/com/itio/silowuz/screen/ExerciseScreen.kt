package com.itio.silowuz.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        LazyColumn{
            items(seriesList){ series ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = stringResource(
                                id = R.string.series_info_reps,
                                series.reps
                            ),
                                modifier = Modifier
                                    .padding(16.dp)
                            )
                            Text(
                                text = stringResource(
                                id = R.string.series_info_series,
                                series.series
                            ),
                                modifier = Modifier
                                    .padding(16.dp)
                            )
                            Text(
                                text = stringResource(
                                id = R.string.series_info_weight,
                                series.weight
                            ),
                                modifier = Modifier
                                    .padding(16.dp)
                            )
                        }
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .width(IntrinsicSize.Min),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {},
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(text = "Usuń")
                            }
                            Button(
                                onClick = {},
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(text = "Edytuj")
                            }
                        }
                    }

                }
            }
        }
        Spacer(Modifier.weight(1f))
        Button(
            modifier = Modifier
                .fillMaxWidth(),

            onClick = { showExerciseDialog = true}
        ) {
            Text(text = "Add new series")
        }

        if(showExerciseDialog){
            Dialog(onDismissRequest = {showExerciseDialog = false}){
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    tonalElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = "Informations about series")
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(onClick = {showExerciseDialog = false}) {
                                Text(text = "Cancel")
                            }
                            Button(onClick = {
                                seriesList = seriesList + ExerciseSeries(10,3,99.9)
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