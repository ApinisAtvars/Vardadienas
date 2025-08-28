package com.example.vardadienas.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vardadienas.data.entities.FavouriteNameDayReminder
import com.example.vardadienas.data.valueClasses.MonthDay
import com.example.vardadienas.ui.viewModels.FavouritesViewModel

@Composable
fun FavouriteScreen(viewModel: FavouritesViewModel = viewModel()) {
    val favourites by viewModel.favourites
    val areFavouritesLoading by viewModel.isFavouritesLoading

    Button(onClick = { viewModel.fetchAllFavourites() }) {
        Text(text = "Refresh")
    }
    Button(onClick = { viewModel.addFavourite(FavouriteNameDayReminder(null, 1, MonthDay.fromString("01-01"))) })
    {
        Text(text = "Add")
    }

    Column(
        Modifier.fillMaxSize().padding(16.dp)
    ) {
        if (areFavouritesLoading) {
            LinearProgressIndicator(Modifier.fillMaxWidth().padding(10.dp))
        } else {
            favourites.forEach { it ->
                Row (Modifier.fillMaxWidth().padding(horizontal = 10.dp), horizontalArrangement = Arrangement.Center) {
                    Text(text = it.nameDay.name, textAlign = TextAlign.Left)
                    Text(text = it.nameDay.date.toString(), textAlign = TextAlign.Right)
                }
                it.reminders.forEach {
                    Text(text = it.dateToRemind.toString()) // TODO: Not allow multiple identical reminders (at same date to remind)
                }
                HorizontalDivider()

            }
        }
    }
}