package com.itio.silowuz.screen

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.itio.silowuz.R
import androidx.core.net.toUri
import android.Manifest
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.core.content.ContextCompat
import com.itio.silowuz.data.TrainingReminderPrefs
import com.itio.silowuz.services.TrainingReminderScheduler


@Composable
fun ProfileScreen(paddingValues: PaddingValues, onLogout: () -> Unit){
    val currentLocales = AppCompatDelegate.getApplicationLocales()
    val isCurrentlyPolish = currentLocales.toLanguageTags().contains("pl")
    var isPolish by remember { mutableStateOf(isCurrentlyPolish) }
    val context = LocalContext.current

    val reminderPrefs = remember { TrainingReminderPrefs(context) }

    var reminderEnabled by remember { mutableStateOf(reminderPrefs.enabled) }
    var reminderHour by remember { mutableStateOf(reminderPrefs.hour) }
    var reminderMinute by remember { mutableStateOf(reminderPrefs.minute) }

    fun enableAndSchedule() {
        reminderEnabled = true
        reminderPrefs.enabled = true
        TrainingReminderScheduler.schedule(context, reminderHour, reminderMinute)
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            enableAndSchedule()
        } else {
            reminderEnabled = false
            reminderPrefs.enabled = false
            TrainingReminderScheduler.cancel(context)
        }
    }


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        IconButton(
            onClick = {
                isPolish = !isPolish
                val newLang = if (isPolish) "pl" else "en"
                val appLocale = LocaleListCompat.forLanguageTags(newLang)
                AppCompatDelegate.setApplicationLocales(appLocale)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(
                    id = if (isPolish) R.drawable.ic_flag_pl else R.drawable.ic_flag_en
                ),
                contentDescription = stringResource(R.string.toggle_language),
                modifier = Modifier.size(40.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 0.dp)
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.training_reminder_label))
            Switch(
                checked = reminderEnabled,
                onCheckedChange = { checked ->
                    if (checked) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            enableAndSchedule()
                        }
                    } else {
                        reminderEnabled = false
                        reminderPrefs.enabled = false
                        TrainingReminderScheduler.cancel(context)
                    }
                }
            )
        }

        Button(
            onClick = {
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        reminderHour = hour
                        reminderMinute = minute
                        reminderPrefs.hour = hour
                        reminderPrefs.minute = minute

                        if (reminderEnabled) {
                            TrainingReminderScheduler.schedule(context, hour, minute)
                        }
                    },
                    reminderHour,
                    reminderMinute,
                    true
                ).show()
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(stringResource(R.string.training_reminder_time, reminderHour, reminderMinute))
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(text = stringResource(R.string.where_to_start))
        Text(text = stringResource(R.string.check_nearest_gym))
        IconButton(
            onClick = {
                findNearestGym(context)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.map_ico),
                contentDescription = stringResource(R.string.map),
                modifier = Modifier.size(64.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))


        Button(
            onClick = onLogout,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(text = stringResource(R.string.logout))
        }
    }

}

fun findNearestGym(context: Context) {
    val query = context.getString(R.string.gym)

    val mapUri = "geo:0,0?q=$query".toUri()
    val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)

    mapIntent.setPackage("com.google.android.apps.maps")

    try {
        context.startActivity(mapIntent)
    } catch (e: ActivityNotFoundException) {
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            "https://www.google.com/maps/search/?api=1&query=$query".toUri()
        )
        context.startActivity(webIntent)
    }
}