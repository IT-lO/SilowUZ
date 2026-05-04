package com.itio.silowuz.screen

/**
 * Main screen for managing training plans and exercises.
 * Provides a tabbed interface allowing users to switch between viewing their plans 
 * and managing individual exercises. Includes functionality for creating, editing, 
 * deleting, importing via Bluetooth, and exporting data.
 */
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.itio.silowuz.R
import com.itio.silowuz.component.exercise.BluetoothSocket
import com.itio.silowuz.component.exercise.ExerciseCard
import com.itio.silowuz.component.exercise.ModeSwitch
import com.itio.silowuz.component.exercise.PlanCard
import com.itio.silowuz.component.exercise.ExerciseDialog
import com.itio.silowuz.component.exercise.PlanDialog
import com.itio.silowuz.dataclass.exercise.Exercise
import com.itio.silowuz.dataclass.exercise.TrainingPlan
import com.itio.silowuz.`interface`.IconResource
import com.itio.silowuz.ui.theme.MainGreen
import com.itio.silowuz.ui.theme.White
import com.itio.silowuz.viewmodel.PlansViewModel

/**
 * Composable function that displays the main plans and exercises screen.
 * This is the central hub for managing training content with features including:
 * - Tabbed view switching between Plans and Exercises modes
 * - Creating, editing, and deleting exercises and plans
 * - Bluetooth import/export functionality for data transfer
 * 
 * @param paddingValues Padding values to apply around the screen content
 * @param viewModel The PlansViewModel instance for managing plans and exercises data (uses default.viewModel() if not provided)
 * @param onStartTraining Callback invoked when a user starts a training plan
 */
@Composable
fun PlansScreen(
    paddingValues: PaddingValues,
    viewModel: PlansViewModel = viewModel(),
    onStartTraining: (TrainingPlan) -> Unit
){
    val bluetoothOff = stringResource(R.string.turn_bluetooth_on)
    val permissionsGranted = stringResource(R.string.permissions_granted)
    val awaitingConnection = stringResource(R.string.awaiting_connection)
    val successfullSent = stringResource(R.string.successfull_sent)
    val sentError = stringResource(R.string.sent_error)
    val planImported = stringResource(R.string.plan_imported)
    val importError = stringResource(R.string.impoort_error)
    var fillAllFieldsText = stringResource(R.string.fill_all_fields)

    var showExerciseDialog by remember { mutableStateOf(false) }
    var showPlanDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var plansMode by remember { mutableStateOf(true) }
    var exerciseToEdit by remember { mutableStateOf<Exercise?>(null) }
    var planToEdit by remember { mutableStateOf<TrainingPlan?>(null) }

    val addExerciseIcon : IconResource = IconResource.Drawable(R.drawable.add_exercise_ico)
    val addSeriesIcon : IconResource = IconResource.Drawable(R.drawable.add_series_ico)
    val iconRotation by animateFloatAsState(targetValue = if (showMenu) 45f else 0f)
    val context = LocalContext.current
    val bluetoothAdapter: BluetoothAdapter? = remember { BluetoothAdapter.getDefaultAdapter() }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.BLUETOOTH_CONNECT] == true) {
            Toast.makeText(context, permissionsGranted, Toast.LENGTH_SHORT).show()
        }
    }
    val checkAndRunBluetooth = { action: () -> Unit ->
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_ADVERTISE,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        } else {
            arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }

        val allGranted = permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        if (allGranted) {
            action()
        } else {
            launcher.launch(permissions)
        }
    }
    var showBluetoothImportDialog by remember { mutableStateOf(false) }
    val pairedDevices = remember(showBluetoothImportDialog) {
        if (showBluetoothImportDialog) {
            bluetoothAdapter?.bondedDevices?.map { it } ?: emptyList()
        } else {
            emptyList()
        }
    }

    viewModel.fetchData()

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
                horizontalAlignment = Alignment.End,
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


                    ){
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = stringResource(R.string.import_))
                            Spacer(modifier = Modifier.width(8.dp))
                            SmallFloatingActionButton(
                                onClick = {
                                   checkAndRunBluetooth {
                                       if (bluetoothAdapter?.isEnabled == true) {
                                           showBluetoothImportDialog = true
                                           showMenu = false
                                       } else {
                                           Toast.makeText(context, bluetoothOff, Toast.LENGTH_SHORT).show()
                                       }
                                   }
                                },
                                containerColor = MainGreen,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = stringResource(R.string.import_),
                                    tint = White
                                )
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = stringResource(R.string.create_exercise))
                            Spacer(modifier = Modifier.width(8.dp))
                            SmallFloatingActionButton(
                                containerColor = MainGreen,
                                onClick = { showExerciseDialog = true},
                            ) {
                                when(addExerciseIcon){
                                    is IconResource.Drawable -> {
                                        Icon(
                                            painter = painterResource(id = addExerciseIcon.resId),
                                            contentDescription = stringResource(R.string.create_exercise),
                                            tint = White

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
                                containerColor = MainGreen,
                                onClick = { showPlanDialog = true },
                            ) {
                                when(addSeriesIcon){
                                    is IconResource.Drawable -> {
                                        Icon(
                                            painter = painterResource(id = addSeriesIcon.resId),
                                            contentDescription = stringResource(R.string.create_plan),
                                            tint = White
                                        )
                                    }
                                    else -> { }
                                }
                            }
                        }
                    }
                }
                FloatingActionButton(
                    containerColor = MainGreen,
                    onClick = { showMenu = !showMenu },
                ) {
                    Icon(Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.exercise_menu),
                        modifier = Modifier.rotate(iconRotation),
                        tint = White
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
                    PlanCard(
                        trainingPlan = plan,
                        allExercises = viewModel.exercises,
                        onStartTraining = { onStartTraining(plan) },
                        onEdit = { planToEdit = plan },
                        onDelete = { viewModel.deletePlan(plan.id) },
                        onExport = {
                            checkAndRunBluetooth {
                                val discoverableIntent = android.content.Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                                    putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
                                }
                                context.startActivity(discoverableIntent)

                                bluetoothAdapter?.let { adapter ->
                                    Toast.makeText(context, awaitingConnection, Toast.LENGTH_SHORT).show()
                                    viewModel.startBluetoothExport(adapter, plan) { success ->
                                        val msg = if (success) successfullSent else sentError
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
    if (showBluetoothImportDialog) {
        AlertDialog(
            onDismissRequest = { showBluetoothImportDialog = false },
            title = { Text(stringResource(R.string.choose_device_to_connect)) },
            text = {
                if (pairedDevices.isEmpty()) {
                    Text(stringResource(R.string.no_paired_devices_pair_in_system))
                } else {
                    LazyColumn {
                        items(pairedDevices) { device ->
                            Text(
                                text = device.name ?: stringResource(R.string.unknown_device),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showBluetoothImportDialog = false

                                        viewModel.importPlanFromDevice(device) { success ->
                                            if (success) {
                                                Toast.makeText(context, planImported, Toast.LENGTH_SHORT).show()
                                            } else {
                                                Toast.makeText(context, importError, Toast.LENGTH_LONG).show()
                                            }
                                        }
                                    }
                                    .padding(16.dp)
                            )
                            HorizontalDivider()
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showBluetoothImportDialog = false }) {
                    Text(stringResource(R.string.close))
                }
            }
        )
    }
    if (showExerciseDialog || exerciseToEdit != null) {
        ExerciseDialog(
            exercise = exerciseToEdit,
            onDismissRequest = {
                showExerciseDialog = false
                exerciseToEdit = null
            },
            onSave = { name, reps, sets, weight, duration ->
                if (isExerciseValid(name, reps.toString(), sets.toString())) {
                    viewModel.saveExercise(Exercise(exerciseToEdit?.id ?: "", name, reps, sets, weight, duration))
                    showExerciseDialog = false
                    exerciseToEdit = null
                } else {
                    Toast.makeText(context, fillAllFieldsText, Toast.LENGTH_SHORT).show()
                }
            },
            paddingValues = paddingValues
        )
    }

    if (showPlanDialog || planToEdit != null) {
        PlanDialog(
            planToEdit = planToEdit,
            availableExercises = viewModel.exercises,
            onDismissRequest = {
                showPlanDialog = false
                planToEdit = null
            },
            onSave = { name, selectedIds ->
                if (planToEdit == null) {
                    viewModel.savePlan(name, selectedIds)
                } else {
                    viewModel.updatePlan(planToEdit!!.copy(name = name, exerciseIds = selectedIds))
                }
                showPlanDialog = false
                planToEdit = null
            }
        )
    }
}

/**
 * Validates exercise data for saving to Firebase.
 * Checks that all required fields have valid values before persisting.
 * 
 * @param name Exercise name - must not be blank (at least one non-whitespace character)
 * @param reps Number of repetitions - must be a valid integer
 * @param sets Number of sets - must be a valid integer
 * @return True if all fields are valid, false otherwise
 */
fun isExerciseValid(name: String, reps: String, sets: String): Boolean {
    return name.isNotBlank() &&
            reps.toIntOrNull() != null &&
            sets.toIntOrNull() != null
}
