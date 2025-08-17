package com.example.vardadienas.data

import androidx.room.TypeConverter

import com.example.vardadienas.data.valueClasses.MonthDay

class Converters {
    @TypeConverter
    fun fromMonthDay(monthDay: MonthDay?): Int? {
        // Converts our custom class to an Int for the database
        return monthDay?.value
    }

    @TypeConverter
    fun toMonthDay(value: Int?): MonthDay? {
        // Converts an Int from the database back to our custom class
        return value?.let { MonthDay(it) }
    }
}