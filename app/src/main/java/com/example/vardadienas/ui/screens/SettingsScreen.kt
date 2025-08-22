package com.example.vardadienas.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vardadienas.ui.viewModels.SettingsViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val areNotificationsEnabled by settingsViewModel.areDailyNotificationsEnabled.collectAsStateWithLifecycle()
    val notificationTime by settingsViewModel.notificationTime.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // State to control our custom rationale dialog
    var showRationaleDialog by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) } // State for time picker

    val timePickerState = rememberTimePickerState(
        initialHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
        initialMinute = Calendar.getInstance().get(Calendar.MINUTE),
        is24Hour = true,
    )

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Permission was granted by the user. Now enable the feature.
                settingsViewModel.onDailyNotificationSettingChanged(true)
            } else {
                // Permission was denied. The switch will remain off.
                // You could show a snackbar here explaining that the feature can't be used.
            }
        }
    )

    // Show the dialog when its state is true
    if (showRationaleDialog) {
        NotificationPermissionRationaleDialog(
            onConfirm = {
                // User confirmed, now launch the system permission request
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            },
            onDismiss = {
                showRationaleDialog = false
            }
        )
    }

    if (showTimePicker) {
        NotificationTimePickerDialog(
            onConfirm = {settingsViewModel.onNotificationTimeChanged(timePickerState.hour, timePickerState.minute); showTimePicker = false; Log.d("SettingsScreen", "${settingsViewModel.notificationTime.value.first}:${settingsViewModel.notificationTime.value.second}")},
            onDismiss = { showTimePicker = false },
            timePickerState = timePickerState
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Dienišķie vārdadienu atgādinājumi", style = MaterialTheme.typography.bodyLarge)
            Switch(
                checked = areNotificationsEnabled,
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        // User is trying to TURN ON notifications
                        val permissionStatus = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                            // Permission is already granted, just enable the setting
                            settingsViewModel.onDailyNotificationSettingChanged(true)
                        } else {
                            // Permission not granted, show our rationale dialog first
                            showRationaleDialog = true
                        }
                    } else {
                        // User is turning OFF notifications, no permission check needed
                        settingsViewModel.onDailyNotificationSettingChanged(false)
                    }
                }
            )
        }
        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
        Row(
            modifier = Modifier
                .fillMaxWidth()
//                .clickable(enabled = areNotificationsEnabled) { showTimePicker = true }
                .padding(vertical = 8.dp)
                .alpha(if (areNotificationsEnabled) 1f else 0.5f), // Gray out when disabled
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
                Text("Atgādinājumu laiks:", style = MaterialTheme.typography.bodyLarge)
                TextButton(onClick = { showTimePicker = true }, enabled = areNotificationsEnabled) {
                    Text(text = "${notificationTime.first}:${notificationTime.second}", style = MaterialTheme.typography.labelLarge)
                }
        }
    }
}

@Composable
private fun NotificationPermissionRationaleDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Ļauj man paziņot!") },
        text = {
            Text(
                text = "Lai es varu tev atgādināt par vārda dienām." +
                        "Vai tu piekrīti?"
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm() // User agreed, now we can request permission
                    onDismiss() // Close the dialog
                }
            ) {
                Text("Jā")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) { // User declined
                Text("Nē")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationTimePickerDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    timePickerState: TimePickerState
) {
    Box (Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column {
            TimeInput(
                state = timePickerState,
            )
            Button(onClick = onDismiss) {
                Text("Atcelt")
            }
            Button(onClick = onConfirm) {
                Text("Labiņi")
            }
        }
    }
}