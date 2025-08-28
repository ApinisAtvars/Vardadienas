package com.example.vardadienas.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.vardadienas.data.valueClasses.MonthDay

@Entity(tableName = "favouriteNameDays", foreignKeys = [ForeignKey(entity = NameDay::class, parentColumns = ["nameDayId"],
    childColumns = ["nameDayId"]
)]
)
data class FavouriteNameDayReminder(
    /* I did a very dumb thing making two separate tables for name days and extended name days.
    * So the reminders for upcoming name days will work only with primary name days (the ones in the calendar)
    * I don't know where the guy got the extended name days, but they are also not in the PMLP data, so maybe it's good
    * that they're left out.
    */
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val nameDayId: Int,
    val dateToRemind: MonthDay
)

// Relationship model
data class NameDayWithFavourites(
    @Embedded val nameDay: NameDay,
    @Relation(
        parentColumn = "nameDayId",
        entityColumn = "nameDayId"
    )
    val reminders: List<FavouriteNameDayReminder>
)