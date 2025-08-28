package com.example.vardadienas.data.repositories

import android.content.Context
import android.util.Log
import com.example.vardadienas.data.Converters
import com.example.vardadienas.data.MonthDayAdapter
import com.example.vardadienas.data.NameDayDatabase
import com.example.vardadienas.data.entities.FavouriteNameDayReminder
import com.example.vardadienas.data.entities.NameDay
import com.example.vardadienas.data.entities.NameDayExtended
import com.example.vardadienas.data.entities.NameDayWithFavourites
import com.example.vardadienas.data.valueClasses.MonthDay
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import kotlin.math.log

// The repository takes the application context to be able to get the database instance.
class NameDayRepository(context: Context) {

    // Get a reference to the DAOs. This is where the database is actually initialized on first call.
    private val nameDayDao = NameDayDatabase.Companion.getDatabase(context.applicationContext).nameDayDao()
    private val nameDayExtendedDao = NameDayDatabase.Companion.getDatabase(context.applicationContext).nameDayExtendedDao()
    private val favouriteNameDayDao = NameDayDatabase.Companion.getDatabase(context.applicationContext).favouriteNameDayDao()

    // Main Name Days
    fun getAllNameDays(): List<NameDay> {
        return nameDayDao.getAllNameDays()
    }

    fun getNameDaysBetweenDates(minDate: MonthDay, maxDate: MonthDay): List<NameDay> {
        return nameDayDao.getNameDaysBetweenDates(minDate, maxDate)
    }

    fun getNameDayByName(name: String): List<NameDay> {
        return nameDayDao.getNameDayByName(name)
    }

    suspend fun getNameDayByDate(date: String): List<NameDay> {
        return nameDayDao.getNameDayByDate(MonthDay.fromString(date))
    }

    suspend fun getNameDayForToday(): List<NameDay> {
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd"))

        return getNameDayByDate(today)
    }


    // Extended Name Days
    fun getAllExtendedNameDays(): List<NameDayExtended> {
        return nameDayExtendedDao.getAllNameDays()
    }

    fun getExtendedNameDaysBetweenDates(minDate: MonthDay, maxDate: MonthDay): List<NameDayExtended> {
        return nameDayExtendedDao.getNameDaysBetweenDates(minDate, maxDate)
    }

    fun getExtendedNameDayByName(name: String): List<NameDayExtended> {
        return nameDayExtendedDao.getNameDayByName(name)
    }

    fun getExtendedNameDayByDate(date: MonthDay): List<NameDayExtended> {
        return nameDayExtendedDao.getNameDayByDate(date)
    }

    // Favourite name days (Reminders)
    fun getAllFavourites(): List<NameDayWithFavourites> {
        return favouriteNameDayDao.getAllFavourites()
    }

    fun addFavourite(newFavourite: FavouriteNameDayReminder) {
        return favouriteNameDayDao.addFavourite(newFavourite)
    }

    fun removeFavourite(favouriteToRemove: FavouriteNameDayReminder) {
        return favouriteNameDayDao.removeFavourite(favouriteToRemove)
    }
}