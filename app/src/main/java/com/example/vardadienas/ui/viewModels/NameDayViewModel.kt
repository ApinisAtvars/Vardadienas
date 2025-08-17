package com.example.vardadienas.ui.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.vardadienas.data.entities.NameDay
import com.example.vardadienas.data.repositories.NameDayRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Use AndroidViewModel so we can get the application context.
class NameDayViewModel(application: Application) : AndroidViewModel(application) {

    // Create an instance of the repository.
    private val repository = NameDayRepository(application)

    // Use LiveData to hold the list of name days for the UI.
    // LiveData is lifecycle-aware and automatically updates the UI.
    private val _nameDays = MutableLiveData<List<NameDay>>()
    val nameDays: LiveData<List<NameDay>> = _nameDays

    // Function to be called from the UI to fetch data.
    fun findNameDaysForToday() {
        // Use viewModelScope to launch a coroutine. It's automatically cancelled
        // when the ViewModel is cleared.
        viewModelScope.launch {
            // Switch to a background thread to call the repository (which hits the DB).
            val result = withContext(Dispatchers.IO) {
                repository.getNameDayForToday()
            }
            // Post the result to LiveData on the main thread.
            _nameDays.value = result
        }
    }
}