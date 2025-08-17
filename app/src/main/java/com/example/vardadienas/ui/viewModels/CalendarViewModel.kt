package com.example.vardadienas.ui.viewModels

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vardadienas.data.entities.NameDay
import com.example.vardadienas.data.repositories.NameDayRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CalendarViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NameDayRepository(application)

    // Expose the list of name days as a Compose State
    private val _nameDays = mutableStateOf<List<NameDay>>(emptyList())
    val nameDays: State<List<NameDay>> = _nameDays

    // You can also expose loading/error states for a better UX
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    /**
     * Fetches name days for a given date string ("MM-dd").
     * This function will run the database query on a background thread.
     */
    fun fetchNameDaysForDate(dateString: String) {
        // Use viewModelScope to launch a coroutine that is tied to this ViewModel's lifecycle
        viewModelScope.launch {
            _isLoading.value = true // Show a loading indicator in the UI
            try {
                // Switch to a background thread for the database call
                val result = withContext(Dispatchers.IO) {
                    repository.getNameDayByDate(dateString)
                }
                // Update the state on the main thread
                _nameDays.value = result
            } catch (e: Exception) {
                // Handle potential errors, e.g., show a toast
                _nameDays.value = emptyList() // Clear data on error
            } finally {
                _isLoading.value = false // Hide the loading indicator
            }
        }
    }
}