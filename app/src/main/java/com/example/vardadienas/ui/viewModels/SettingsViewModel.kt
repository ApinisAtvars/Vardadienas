package com.example.vardadienas.ui.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vardadienas.data.repositories.NameDayRepository
import com.example.vardadienas.data.stores.SettingsDataStore
import com.example.vardadienas.helpers.NameDayNotifier
import com.example.vardadienas.schedulers.NotificationScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// This ViewModel requires the Application context to initialize its helpers,
// so it inherits from AndroidViewModel.
class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    // 1. Initialize the helpers that the ViewModel will use.
    private val settingsDataStore = SettingsDataStore(application)
    private val notificationScheduler = NotificationScheduler(application)

    // 2. Expose the user's preference as a StateFlow for the UI to observe.
    //    The .stateIn operator converts the "cold" Flow from DataStore into a "hot" StateFlow,
    //    which is ideal for UI state. It remembers the last value.
    val areDailyNotificationsEnabled = settingsDataStore.dailyNotificationsEnabled
        .stateIn(
            scope = viewModelScope, // The flow's lifecycle is tied to the ViewModel's.
            started = SharingStarted.WhileSubscribed(5000), // Keeps the flow active for 5s after UI leaves.
            initialValue = false // The default value before DataStore has loaded.
        )

    val notificationTime = settingsDataStore.notificationTime
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Pair(9, 0))

    /**
     * This is the function the UI calls when the user toggles the notification switch.
     *
     * @param isEnabled The new state of the switch.
     */
    fun onDailyNotificationSettingChanged(isEnabled: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setDailyNotificationsEnabled(isEnabled)
            if (isEnabled) {
                // When enabling, schedule based on the currently saved time
                val time = notificationTime.value // We can use the StateFlow's current value
                notificationScheduler.scheduleDailyNameDayNotification(time.first, time.second)
            } else {
                notificationScheduler.cancelDailyNameDayNotification()
            }
        }
    }

    // NEW: Function to handle the user selecting a new time
    fun onNotificationTimeChanged(hour: Int, minute: Int) {
        viewModelScope.launch {
            // Save the new time
            settingsDataStore.saveNotificationTime(hour, minute)
            // If notifications are enabled, reschedule the work with the new time
            if (areDailyNotificationsEnabled.value) {
                notificationScheduler.scheduleDailyNameDayNotification(hour, minute)
            }
        }
    }
}