package com.example.vardadienas.ui // <-- IMPORTANT: Make sure this package is correct!

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vardadienas.R
import com.example.vardadienas.data.PersonNameData
import com.example.vardadienas.ui.theme.VardaDienasTheme

// 1. Define custom font family here (This part stays the same)
val NothingFontFamily = FontFamily(
    Font(R.font.nothing_font)
)

class MainActivity : ComponentActivity() {

    // 2. Get an instance of the ViewModel
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VardaDienasTheme {
                // 3. Observe the state from the ViewModel
                val uiState by viewModel.uiState.observeAsState(initial = MainViewModel.UiState.Idle)

                Surface(modifier = Modifier.fillMaxSize()) {
                    // 4. Pass the state and the search function to our main screen
                    NameSearchScreen(
                        uiState = uiState,
                        onSearchClicked = { name ->
                            viewModel.searchForName(name)
                        }
                    )
                }
            }
        }
    }
}

// THE MAIN UI COMPOSABLE
@Composable
fun NameSearchScreen(
    uiState: MainViewModel.UiState,
    onSearchClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // This state is managed by the Composable itself for the text field
    var text by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Personvārdu datubāze",
            fontSize = 24.sp,
            fontFamily = NothingFontFamily
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- Input Fields ---
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Ievadiet vārdu") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onSearchClicked(text) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Meklēt")
        }
        Spacer(modifier = Modifier.height(24.dp))

        // --- Content Area that changes based on state ---
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is MainViewModel.UiState.Idle -> {
                    Text("Lūdzu, ievadiet vārdu, lai sāktu meklēšanu.", textAlign = TextAlign.Center)
                }
                is MainViewModel.UiState.Loading -> {
                    CircularProgressIndicator()
                }
                is MainViewModel.UiState.Success -> {
                    // Show the successful result
                    PersonDataDisplay(data = uiState.data)
                }
                is MainViewModel.UiState.Error -> {
                    Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

// A DEDICATED COMPOSABLE FOR DISPLAYING THE RESULTS
@Composable
fun PersonDataDisplay(data: PersonNameData, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        DataRow(label = "Vārds", value = data.name)
        DataRow(label = "Sastopams", value = data.amount.toString())
        DataRow(label = "Vārdadiena", value = if (data.nameDay.isNotBlank()) data.nameDay else "-")
        DataRow(label = "Skaidrojums", value = data.explanation)
    }
}

@Composable
fun DataRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = "$label:",
            modifier = Modifier.weight(0.4f),
            fontWeight = FontWeight.Bold,
            fontFamily = NothingFontFamily
        )
        Text(
            text = value,
            modifier = Modifier.weight(0.6f),
            fontFamily = NothingFontFamily
        )
    }
    Divider()
}


// A NEW, MORE USEFUL PREVIEW
@Preview(showBackground = true)
@Composable
fun NameSearchScreenPreview_Success() {
    VardaDienasTheme {
        val successData = PersonNameData(
            name = "ATVARS",
            amount = 195,
            nameDay = "31.03",
            explanation = "Nekad nerunā neko lieku, un viņam piemīt prasme izvēlēties draugus."
        )
        NameSearchScreen(
            uiState = MainViewModel.UiState.Success(successData),
            onSearchClicked = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NameSearchScreenPreview_Loading() {
    VardaDienasTheme {
        NameSearchScreen(
            uiState = MainViewModel.UiState.Loading,
            onSearchClicked = {}
        )
    }
}