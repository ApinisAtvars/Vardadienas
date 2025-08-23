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

    private val settingsDataStore = SettingsDataStore(application)
    private val notificationScheduler = NotificationScheduler(application)

    val areDailyNotificationsEnabled = settingsDataStore.dailyNotificationsEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // REMOVED: notificationTime state and onNotificationTimeChanged function

    fun onDailyNotificationSettingChanged(isEnabled: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setDailyNotificationsEnabled(isEnabled)
            if (isEnabled) {
                // The scheduler now handles all the timing logic internally
                notificationScheduler.scheduleDailyNameDayNotification()
            } else {
                notificationScheduler.cancelDailyNameDayNotification()
            }
        }
    }
}