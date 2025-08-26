package com.example.vardadienas.ui.viewModels

import android.app.Application
import android.util.Log
import com.example.vardadienas.data.repositories.PMLPrepository
import com.example.vardadienas.data.repositories.PersonNameInList

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SearchNameViewModel(application: Application) : AndroidViewModel(application) {
    private val pmlpRespository = PMLPrepository()

    private val _allListNames = mutableStateOf<List<PersonNameInList>>(emptyList())
    val allListNames: State<List<PersonNameInList>> = _allListNames

    // Expose loading/error states
    private val _areAllListNamesLoading = mutableStateOf(false)
    val areAllListNamesLoading: State<Boolean> = _areAllListNamesLoading

    fun fetchListOfNames(personName: String) {
        viewModelScope.launch {
            _areAllListNamesLoading.value = true
            try {
                val result = withContext(Dispatchers.IO) {
                    pmlpRespository.fetchListOfPersonNames(personName)
                }
                _allListNames.value = result.getOrNull() ?: emptyList()
                } catch (e: Exception) {
                    _allListNames.value = emptyList()
                    Log.e("SearchNameViewModel", "Error fetching list of names: ${e.message}")
                } finally {
                    _areAllListNamesLoading.value = false

            }
        }
    }
}