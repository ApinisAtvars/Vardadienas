package com.example.vardadienas.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vardadienas.data.PMLPrepository
import com.example.vardadienas.data.PersonNameData
import kotlinx.coroutines.launch

// The class must extend ViewModel
class MainViewModel : ViewModel() {

    private val repository = PMLPrepository()

    // Sealed class for a clean UI State
    sealed class UiState {
        object Idle : UiState() // The state before any search
        object Loading : UiState()
        data class Success(val data: PersonNameData) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _uiState = MutableLiveData<UiState>(UiState.Idle)
    val uiState: LiveData<UiState> = _uiState

    fun searchForName(name: String) {
        if (name.isBlank()) {
            _uiState.value = UiState.Error("Name cannot be empty.")
            return
        }

        _uiState.value = UiState.Loading
        viewModelScope.launch {
            val result = repository.fetchPersonNameData(name)
            result.onSuccess { personData ->
                _uiState.value = UiState.Success(personData)
            }.onFailure { exception ->
                _uiState.value = UiState.Error(exception.message ?: "An unknown error occurred.")
            }
        }
    }
}