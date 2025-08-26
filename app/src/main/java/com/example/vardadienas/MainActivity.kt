package com.example.vardadienas

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.vardadienas.helpers.NameDayNotifier
import com.example.vardadienas.ui.theme.NothingDark // Make sure this matches your theme's package
import com.example.vardadienas.ui.viewModels.NameDayViewModel

class MainActivity : ComponentActivity() {
    // Get a reference to the ViewModel using the AndroidX activity-ktx delegate.
    private val nameDayViewModel: NameDayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NameDayNotifier(this).createNotificationChannel()
//        nameDayViewModel.nameDays.observe(this) { names ->
//            Log.d("MainActivity", "Name days for today: $names")
//        }
        setContent {
            NothingDark {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // This is the entry point for our entire UI
                    MainScreen()
                }
            }
        }
        nameDayViewModel.findNameDaysForToday()
    }
}