package com.example.vardadienas

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

// Sealed class to define our app's screens
sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Welcome : Screen("welcome", "Sākums", Icons.Default.Home)
    object Favourites : Screen("favourites", "Favorītie", Icons.Default.Star)
    object Search : Screen("search", "Meklēt", Icons.Default.Search)
    object Calendar : Screen("calendar", "Kalendārs", Icons.Default.DateRange)
    object Settings : Screen("settings", "Uzstādījumi", Icons.Default.Settings)
    object About : Screen("about", "Informācija", Icons.Default.Info)
}

// A list of all our screens for easy access in the UI
val appScreens = listOf(
    Screen.Welcome,
    Screen.Favourites,
    Screen.Search,
    Screen.Calendar,
    Screen.Settings,
    Screen.About
)