package com.example.vardadienas.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vardadienas.data.repositories.PersonNameInList
import com.example.vardadienas.ui.viewModels.SearchNameViewModel



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class) // Needed for search bar
@Composable
fun SearchScreen(viewModel: SearchNameViewModel = viewModel()) {
    val allSearchResults by viewModel.allListNames
    val areSearchResultsLoading by viewModel.areAllListNamesLoading

//    LaunchedEffect(allSearchResults) {
//        Log.d("SearchScreen", "allSearchResults: $allSearchResults")
//    }

    Column {
        CustomSearchBar { viewModel.fetchListOfNames(it) }

        LazyColumn {
            if (!areSearchResultsLoading) {
                allSearchResults.forEach { item ->
                    item { SearchResultItem(item) }
                }
            } else {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.background
                    )
                }
            }
        }
    }

}

@Composable
fun SearchResultItem(item: PersonNameInList) {
    Card(Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 4.dp)) {

        Text(text = item.name, style= MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        HorizontalDivider(Modifier.padding(vertical = 4.dp, horizontal = 8.dp))

        Row(Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Sastopams:", style = MaterialTheme.typography.bodyLarge)
            Text(item.amount.toString(), style = MaterialTheme.typography.bodyMedium)
        }

        Row(Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Vārda diena:", style = MaterialTheme.typography.bodyLarge)
            if (!item.nameDay.isNullOrBlank()) { // If name is not null, "", or " "
                Text(item.nameDay, style = MaterialTheme.typography.bodyMedium)
            } else {
                Text("-", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun CustomSearchBar(
    onSearch: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val performSearch = {
        onSearch(text)
        keyboardController?.hide() // Hide the keyboard
        focusManager.clearFocus()  // Remove focus from the TextField, which also hides the cursor
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Ievadi vārdu", style = MaterialTheme.typography.labelLarge) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { performSearch() } // Call performSearch when "Enter" is pressed
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(onClick = { performSearch() }) { // Call performSearch on button click
            Text("Meklēt", style = MaterialTheme.typography.labelLarge)
        }
    }
}