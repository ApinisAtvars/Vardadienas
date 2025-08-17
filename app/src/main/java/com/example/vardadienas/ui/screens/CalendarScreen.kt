package com.example.vardadienas.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.vardadienas.data.entities.NameDay
import com.example.vardadienas.data.repositories.NameDayRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vardadienas.ui.viewModels.CalendarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(viewModel: CalendarViewModel = viewModel()) {
    // 1. Create a state object for the DatePicker.
    // This state holds information like the selected date, the displayed month, etc.
    // We can set an initial selected date if we want.
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = null
    )

    val nameDays by viewModel.nameDays
    val isLoading by viewModel.isLoading

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
                dayContentColor = MaterialTheme.colorScheme.tertiary,
                selectedDayContentColor = MaterialTheme.colorScheme.primary,
                todayDateBorderColor = MaterialTheme.colorScheme.background,
                todayContentColor = MaterialTheme.colorScheme.secondary
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
                        style = MaterialTheme.typography.headlineSmall,
//                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.width(16.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.background
                        )
                    }
                    else {
                        Text(
                            text = nameDays.map { it.name + "\n" }.joinToString { it },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                }
            }
        }

        // You could add other UI elements here below the calendar
        // For example, a button or text that shows the selected date.
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

// BAD BAD BAD do NOT uncomment under ANY CIRCUMSTANCES
// UI thread is NOT responsible for calling the database!!!!
//private fun fetchMainNamedays(namedayRepository: NameDayRepository,monthDayString: String): String {
//    val namedays = namedayRepository.getNameDayByDate(monthDayString)
//
//    return namedays.map { it.name + "\n" }.joinToString { it }
//}


