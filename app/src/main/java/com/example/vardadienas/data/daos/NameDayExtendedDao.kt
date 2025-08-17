package com.example.vardadienas.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vardadienas.data.entities.NameDay
import com.example.vardadienas.data.entities.NameDayExtended
import com.example.vardadienas.data.valueClasses.MonthDay

@Dao
interface NameDayExtendedDao {
    @Query("SELECT * FROM nameDaysExtended ORDER BY date ASC")
    fun getAllNameDays(): List<NameDayExtended>

    @Query("SELECT * FROM nameDaysExtended WHERE date >= :minDate AND date <= :maxDate ORDER BY date ASC")
    fun getNameDaysBetweenDates(minDate: MonthDay, maxDate: MonthDay): List<NameDayExtended>

    @Query("SELECT * FROM nameDaysExtended WHERE name = :name")
    fun getNameDayByName(name: String): List<NameDayExtended>

    @Query("SELECT * FROM nameDaysExtended WHERE date = :date")
    fun getNameDayByDate(date: MonthDay): List<NameDayExtended>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(nameDays: List<NameDayExtended>)
}