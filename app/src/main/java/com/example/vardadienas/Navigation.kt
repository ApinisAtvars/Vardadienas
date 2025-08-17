package com.example.vardadienas

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

// Sealed class to define our app's screens
sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Welcome : Screen("welcome", "Welcome", Icons.Default.Home)
    object Calendar : Screen("calendar", "Calendar", Icons.Default.DateRange)
    object About : Screen("about", "About", Icons.Default.Info)
}

// A list of all our screens for easy access in the UI
val appScreens = listOf(
    Screen.Welcome,
    Screen.Calendar,
    Screen.About
)