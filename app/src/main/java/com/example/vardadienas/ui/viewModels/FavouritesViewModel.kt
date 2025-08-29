package com.example.vardadienas.ui.viewModels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.example.vardadienas.data.entities.NameDayWithFavourites
import com.example.vardadienas.data.repositories.NameDayRepository
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.vardadienas.data.entities.FavouriteNameDayReminder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouritesViewModel(application: Application) : AndroidViewModel(application) {
    private val nameDayRepository = NameDayRepository(application) // Repository also contains function for fetching favourites

    private val _favourites = mutableStateOf<List<NameDayWithFavourites>>(emptyList())
    val favourites: State<List<NameDayWithFavourites>> = _favourites

    private val _isFavouritesLoading = mutableStateOf(false)
    val isFavouritesLoading: State<Boolean> = _isFavouritesLoading

    fun fetchAllFavourites() {
        viewModelScope.launch {
            _isFavouritesLoading.value = true
            try {
                val result = withContext(Dispatchers.IO) {
                    nameDayRepository.getAllFavourites()
                }
                _favourites.value = result
            } catch (e: Exception) {
                Log.d("FavouritesViewModel", "Error fetching favourites: ${e.message}")
                _favourites.value = emptyList()
            } finally {
                _isFavouritesLoading.value = false
            }
        }
    }
    // Add Favourite, but this should only be for adding multiple reminders for one name day
    // E.g. you already have one reminder set to week before, but want to add a second one
    // You should not create reminders for new names from the favourites screen, it's just for an overview and editing.
    fun addFavourite(newFavourite: FavouriteNameDayReminder) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    if (nameDayRepository.checkIdenticalFavourite(newFavourite)) {
                        Log.d("FavouritesViewModel", "Favourite already exists")
                        return@withContext
                    }
                    nameDayRepository.addFavourite(newFavourite)
                }
            } catch (e: Exception) {
                Log.d("FavouritesViewModel", "Error adding favourite: ${e.message}")
            }
        }
    }
    fun removeFavourite(favouriteToRemove: FavouriteNameDayReminder) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    nameDayRepository.removeFavourite(favouriteToRemove)
                }

                // Update the list of all reminders on the UI thread so that database doesn't need to be queried
                val currentList = _favourites.value


                val updatedList = currentList
                    .map { nameDayWithFavs ->
                        nameDayWithFavs.copy(
                            reminders = nameDayWithFavs.reminders.filter { reminder ->
                                reminder.id != favouriteToRemove.id
                            }
                        )
                    }
                    // also remove the entire NameDay entry if it has no reminders left.
                    .filter { nameDayWithFavs ->
                        nameDayWithFavs.reminders.isNotEmpty()
                    }

                _favourites.value = updatedList

            } catch (e: Exception) {
                Log.d("FavouritesViewModel", "Error removing favourite: ${e.message}")
            }
        }
    }

    fun getAllFavourites() {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    nameDayRepository.getAllFavourites()
                }
                _favourites.value = result
            } catch (e: Exception) {
                Log.d("FavouritesViewModel", "Error fetching favourites: ${e.message}")
                _favourites.value = emptyList()
            }
        }
    }


}