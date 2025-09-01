package com.example.vardadienas.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vardadienas.data.entities.FavouriteNameDayReminder
import com.example.vardadienas.data.valueClasses.MonthDay
import com.example.vardadienas.ui.viewModels.FavouritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteScreen(viewModel: FavouritesViewModel = viewModel()) {
    val favourites by viewModel.favourites
    val areFavouritesLoading by viewModel.isFavouritesLoading

        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Button(onClick = { viewModel.fetchAllFavourites() }) {
                Text(text = "Refresh")
            }
            Button(onClick = {
                viewModel.addFavourite(
                    FavouriteNameDayReminder(
                        null,
                        1,
                        MonthDay.fromString("01-01")
                    )
                )
            })
            {
                Text(text = "Add")
            }


            if (areFavouritesLoading) {
                LinearProgressIndicator(Modifier
                    .fillMaxWidth()
                    .padding(10.dp))
            } else {
                favourites.forEach { it ->
                    Card {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = it.nameDay.name,
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = it.nameDay.date.toString(),
                                    textAlign = TextAlign.End,
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                            HorizontalDivider()
                            Text(
                                text = "Atgāginājumi:",
                                style = MaterialTheme.typography.titleLarge
                            )
                            it.reminders.forEach {
                                Card(modifier = Modifier.padding(8.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween, // Pushes label left, value right
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = it.dateToRemind.toString(),
                                            style = MaterialTheme.typography.bodyLarge
                                        )
//                                        Button(onClick = { viewModel.removeFavourite(it) }) {
//                                            Icon(
//                                                imageVector = Icons.Default.Clear,
//                                                contentDescription = "dzēst atgādinājumu"
//                                            )
//                                        }
                                        IconButton(onClick = { viewModel.removeFavourite(it) }) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = "dzēst atgādinājumu"
                                            )
                                        }
                                    }
                                }
                            }

                        }
                    }
                    HorizontalDivider()

                }
            }
        }
}

@Composable
fun ScheduleNewReminderComposable(reminder: FavouriteNameDayReminder, viewModel: FavouritesViewModel = viewModel()) {

}