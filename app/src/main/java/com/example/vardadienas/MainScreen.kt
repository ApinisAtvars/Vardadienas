package com.example.vardadienas

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

import com.example.vardadienas.ui.screens.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    // Controller to manage navigation between screens
    val navController = rememberNavController()

    // State for the navigation drawer (open or closed)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // Coroutine scope to handle drawer animations
    val scope = rememberCoroutineScope()

    // Get the current screen's route to highlight the active item in the drawer
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    

    // This is the main layout structure for the app
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // Add all our screens to the drawer
                appScreens.forEach { screen ->
                    NavigationDrawerItem(
                        label = { Text(text = screen.title, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge) },
                        icon = { Icon(imageVector = screen.icon, contentDescription = screen.title) },
                        selected = (currentRoute == screen.route),
                        onClick = {
                            // When an item is clicked, navigate to that screen
                            navController.navigate(screen.route)
                            // And close the drawer
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )
                }
            }
        }
    ) {
        // The main content of the app
        Scaffold(
            topBar = {
                // The bar at the top of the screen
                TopAppBar(
                    title = {
                        // Find the title for the current screen, default to "Welcome"
                        val title = appScreens.find { it.route == currentRoute }?.title ?: "Welcome"
                        Text(text = title, color = MaterialTheme.colorScheme.primary)
                    },
                    navigationIcon = {
                        // The "hamburger" menu icon to open the drawer
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { paddingValues ->
            // This is where the actual screen content is displayed
            NavHost(
                navController = navController,
                startDestination = Screen.Welcome.route, // The first screen to show
                modifier = Modifier.padding(paddingValues)
            ) {
                // Define the composable for each screen route
                composable(Screen.Welcome.route) { WelcomeScreen() }
                composable(Screen.Calendar.route) { CalendarScreen() }
                composable(Screen.About.route) { AboutScreen() }
            }
        }
    }
}