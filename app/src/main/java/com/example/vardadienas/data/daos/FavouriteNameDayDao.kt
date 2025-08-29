package com.example.vardadienas.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.vardadienas.data.entities.FavouriteNameDayReminder
import com.example.vardadienas.data.entities.NameDayWithFavourites

@Dao
interface FavouriteNameDayDao {
    @Transaction
    @Query("""
        SELECT * FROM nameDays 
        WHERE nameDayId IN (SELECT nameDayId FROM favouriteNameDays)
    """)
    fun getAllFavourites(): List<NameDayWithFavourites>

    @Transaction
    @Query("""
        SELECT * FROM nameDays 
        WHERE nameDayId IN (SELECT nameDayId FROM nameDays WHERE name = :name)
    """)
    fun getAllFavouritesForName(name: String): List<NameDayWithFavourites>

    @Transaction
    @Query("""
        SELECT * FROM nameDays 
        WHERE nameDayId = :namedayId
    """)
    fun getAllFavouritesForNamedayId(namedayId: Int): NameDayWithFavourites

    @Insert(entity = FavouriteNameDayReminder::class)
    fun addFavourite(newFavourite: FavouriteNameDayReminder)

    @Delete(entity = FavouriteNameDayReminder::class)
    fun removeFavourite(favouriteToRemove: FavouriteNameDayReminder)
}
