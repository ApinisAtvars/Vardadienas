package com.example.vardadienas.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

import com.example.vardadienas.data.valueClasses.MonthDay

@Entity(tableName = "nameDays")
data class NameDay(
    @PrimaryKey val nameDayId: Int,
    val name: String,
    val date: MonthDay
)