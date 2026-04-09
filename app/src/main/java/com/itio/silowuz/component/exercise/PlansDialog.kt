package com.itio.silowuz.component.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Dialog
import com.itio.silowuz.dataclass.exercise.Exercise
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itio.silowuz.ui.theme.White

@Composable
fun PlanDialog(
    availableExercises: List<Exercise>,
    onDismissRequest: () -> Unit,
    onSave: (String, List<String>) -> Unit
) {
    var planName by remember { mutableStateOf("") }
    val selectedIds = remember { mutableStateListOf<String>() }

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(White, RoundedCornerShape(15.dp))
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Nowy Plan Treningowy", fontWeight = FontWeight.Bold, fontSize = 20.sp)

            OutlinedTextField(
                value = planName,
                onValueChange = { planName = it },
                label = { "Nazwa planu" },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Wybierz ćwiczenia:", fontWeight = FontWeight.Medium)

            LazyColumn(modifier = Modifier.height(200.dp)) {
                items(availableExercises) { exercise ->
                    val isSelected = selectedIds.contains(exercise.id)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = {
                                if (isSelected) selectedIds.remove(exercise.id)
                                else selectedIds.add(exercise.id)
                            }
                        )
                        Text(exercise.name)
                    }
                }
            }

            Button(
                onClick = { onSave(planName, selectedIds.toList()) },
                modifier = Modifier.align(Alignment.End),
                enabled = planName.isNotBlank() && selectedIds.isNotEmpty()
            ) {
                Text("Zapisz Plan")
            }
        }
    }
}