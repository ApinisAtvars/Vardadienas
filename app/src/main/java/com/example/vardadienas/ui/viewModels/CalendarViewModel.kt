package com.example.vardadienas.ui.viewModels

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vardadienas.data.entities.NameDay
import com.example.vardadienas.data.repositories.NameDayRepository
import com.example.vardadienas.data.repositories.PMLPrepository
import com.example.vardadienas.data.repositories.PersonNameData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CalendarViewModel(application: Application) : AndroidViewModel(application) {
    private val nameDayRepository = NameDayRepository(application)
    private val pmlpRepository = PMLPrepository()

    // Expose the list of name days as a Compose State
    private val _nameDays = mutableStateOf<List<NameDay>>(emptyList())
    val nameDays: State<List<NameDay>> = _nameDays

    // Expose the person name attributes (count, explanation, etc.) as a Compose State
    private val _personNameData = mutableStateOf<PersonNameData?>(null)
    val personNameData: State<PersonNameData?> = _personNameData

    // Expose loading/error states
    private val _isNameDayForDateLoading = mutableStateOf(false)
    val isNameDayForDateLoading: State<Boolean> = _isNameDayForDateLoading
    private val _isPersonNameDataLoading = mutableStateOf(false)
    val isPersonNameDataLoading: State<Boolean> = _isPersonNameDataLoading

    /**
     * Fetches name days for a given date string ("MM-dd").
     * This function will run the database query on a background thread.
     */
    fun fetchNameDaysForDate(dateString: String) {
        // Use viewModelScope to launch a coroutine that is tied to this ViewModel's lifecycle
        viewModelScope.launch {
            _isNameDayForDateLoading.value = true // Show a loading indicator in the UI
            try {
                // Switch to a background thread for the database call
                val result = withContext(Dispatchers.IO) {
                    nameDayRepository.getNameDayByDate(dateString)
                }
                // Update the state on the main thread
                _nameDays.value = result
            } catch (e: Exception) {
                // Handle potential errors, e.g., show a toast
                _nameDays.value = emptyList() // Clear data on error
            } finally {
                _isNameDayForDateLoading.value = false // Hide the loading indicator
            }
        }
    }

    fun fetchPersonNameData(personName: String) {
        viewModelScope.launch {
            try {
                _isPersonNameDataLoading.value = true
                val result = withContext(Dispatchers.IO) {
                    pmlpRepository.fetchPersonNameData(personName)
                }
                _personNameData.value = result.getOrNull()
            } catch (e: Exception) {
                _personNameData.value = null
            } finally {
                _isPersonNameDataLoading.value = false
            }
        }
    }
}