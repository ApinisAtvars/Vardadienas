package com.example.vardadienas.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vardadienas.ui.viewModels.CalendarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(viewModel: CalendarViewModel = viewModel()) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = null
    )

    val openDialog = remember {mutableStateOf(false)}

    val nameDays by viewModel.nameDays
    val isNameDaysForDateLoading by viewModel.isNameDayForDateLoading
    val personNameData by viewModel.personNameData
    val isPersonNameDataLoading by viewModel.isPersonNameDataLoading

    val selectedDateMillis = datePickerState.selectedDateMillis

    LaunchedEffect(selectedDateMillis) {
        // We check if a date is selected (it's not null)
        selectedDateMillis?.let {
            val formattedDate = convertMillisToDate(it)
            Log.d("CalendarScreen", "Selected date: $formattedDate")
            viewModel.fetchNameDaysForDate(formattedDate) // Fetch the name days via ViewModel, which isn't on the UI thread
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top, // Align to the top
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 2. Add the DatePicker composable to the screen.
        // It's a self-contained calendar view.
        DatePicker(
            state = datePickerState,
            modifier = Modifier.padding(top = 16.dp),
            // Hiding the title bar as we already have one in our Scaffold
            title = null,
            headline = null,
            showModeToggle = false, // Hides the button to switch to text input mode
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background,
                selectedDayContainerColor = MaterialTheme.colorScheme.background,
                dayContentColor = MaterialTheme.colorScheme.secondary,
                selectedDayContentColor = MaterialTheme.colorScheme.primary,
                todayDateBorderColor = MaterialTheme.colorScheme.background,
                todayContentColor = MaterialTheme.colorScheme.tertiary
            )
        )
        if (selectedDateMillis != null) {
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${convertMillisToDate(selectedDateMillis).split("-")[1]}. ${convertMillisToMonthName(selectedDateMillis)}",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Galvenās Vārdadienas:",
                        style = MaterialTheme.typography.headlineMedium,
//                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    // Loading State
                    if (isNameDaysForDateLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.width(16.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.background
                        )
                    }

                    // Name days for selected date have loaded
                    else {
                        Column (Modifier.fillMaxWidth().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally   )
                        {
                            for (nameDay in nameDays) {
                                TextButton (onClick = {openDialog.value = true ; viewModel.fetchPersonNameData(nameDay.name)}) {
                                    Text(
                                        text = nameDay.name,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                    }

                    when {
                        openDialog.value -> {
                            Dialog(onDismissRequest = { openDialog.value = false }) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    if (isPersonNameDataLoading) {
                                        Column (
                                            modifier = Modifier.padding(16.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.width(32.dp),
                                                color = MaterialTheme.colorScheme.secondary,
                                                trackColor = MaterialTheme.colorScheme.background
                                            )
                                        }
                                    } else {
                                        if (personNameData != null) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.spacedBy(12.dp) // Adds space between each element
                                            ) {
                                                // 1. Name as the heading
                                                Text(
                                                    text = personNameData!!.name,
                                                    style = MaterialTheme.typography.headlineMedium,
                                                    textAlign = TextAlign.Center
                                                )

                                                // Divider for visual separation
                                                HorizontalDivider(
                                                    modifier = Modifier.padding(vertical = 4.dp),
                                                    thickness = DividerDefaults.Thickness,
                                                    color = DividerDefaults.color
                                                )

                                                // 2. Table-like rows for key-value pairs
                                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                                    InfoRow(label = "Sastopams", value = personNameData!!.amount.toString())
                                                    InfoRow(label = "Vārdadiena", value = personNameData!!.nameDay)
                                                }

                                                // 3. Section for the longer explanation text
                                                Column(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalAlignment = Alignment.Start
                                                ) {
                                                    Text(
                                                        text = "Skaidrojums:",
                                                        style = MaterialTheme.typography.titleSmall,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.padding(bottom = 4.dp)
                                                    )
                                                    Text(
                                                        text = personNameData!!.explanation,
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )
                                                }
                                            }
                                            // --- End of Changed Code ---
                                        } else {
                                            Text(
                                                text = "Nav vairāk informācijas.",
                                                modifier = Modifier.padding(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween, // Pushes label left, value right
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.End
        )
    }
}

private fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM-dd", Locale.getDefault())
    return formatter.format(Date(millis))
}

private fun convertMillisToMonthName(millis: Long): String {
    val latvianMonthNames = listOf(
        "Janvāris", "Februāris", "Marts", "Aprīlis", "Maijs", "Jūnijs",
        "Jūlijs", "Augusts", "Septembris", "Oktobris", "Novembris", "Decembris"
    )

    val monthNumber = convertMillisToDate(millis).split("-")[0].toInt()
    val monthName = latvianMonthNames[monthNumber-1]

    return monthName
}
