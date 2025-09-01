package com.example.vardadienas

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
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
                        },
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
                        // Find the title for the current screen, default to ""
                        val title = appScreens.find { it.route == currentRoute }?.title ?: ""
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
                // Welcome screen without any animations, those are handled within that screen.
                composable(Screen.Welcome.route) { WelcomeScreen() }

                 /*
                 Favourites is a functionality I was working on for a while, but it seemed like too much work for a tiny feature

                 It's very unfinished, and doesn't do anything, but the idea was to set a custom reminder for very special names
                 So you could pick options from the ReminderOption enum, or specify a custom date and maybe time
                 The main problems:
                     1. The notification worker I have for the daily reminders only works when the app is open in the background.
                     2. The worker cannot send a notification when the phone is in Doze mode, so an AlarmManager needs to be used.
                     3. Probably some issues with the extra day every 4 years, which I don't account for.
                 */
//                composable(Screen.Favourites.route) { FavouriteScreen() }

                // Uncomment this, and comment out everything below to remove animations
//                composable(Screen.Search.route) { SearchScreen() }
//                composable(Screen.Calendar.route) { CalendarScreen() }
//                composable(Screen.Settings.route) { SettingsScreen() }
//                composable(Screen.About.route) { AboutScreen() }

                val slideSpec = spring<IntOffset>(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)
                composable(
                    route = Screen.Search.route,
                    enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = slideSpec) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = slideSpec) },
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = slideSpec) },
                    popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = slideSpec) },
                ) { SearchScreen() }

                composable(
                    route = Screen.Calendar.route,
                    enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = slideSpec) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = slideSpec) },
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = slideSpec) },
                    popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = slideSpec) },
                ) { CalendarScreen() }

                composable(
                    route = Screen.Settings.route,
                    enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = slideSpec) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = slideSpec) },
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = slideSpec) },
                    popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = slideSpec) },
                ) { SettingsScreen() }

                composable(
                    route = Screen.About.route,
                    enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = slideSpec) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = slideSpec) },
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = slideSpec) },
                    popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = slideSpec) },
                ) { AboutScreen() }

            }
        }
    }
}