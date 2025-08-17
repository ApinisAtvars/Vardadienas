package com.example.vardadienas.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vardadienas.data.entities.NameDay
import com.example.vardadienas.data.valueClasses.MonthDay

@Dao
interface NameDayDao {
    @Query("SELECT * FROM nameDays ORDER BY date ASC")
    fun getAllNameDays(): List<NameDay>

    @Query("SELECT * FROM nameDays WHERE date >= :minDate AND date <= :maxDate ORDER BY date ASC")
    fun getNameDaysBetweenDates(minDate: MonthDay, maxDate: MonthDay): List<NameDay>

    @Query("SELECT * FROM nameDays WHERE name = :name")
    fun getNameDayByName(name: String): List<NameDay>

    @Query("SELECT * FROM nameDays WHERE date = :date")
    suspend fun getNameDayByDate(date: MonthDay): List<NameDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(nameDays: List<NameDay>)
}