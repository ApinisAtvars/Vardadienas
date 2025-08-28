package com.example.vardadienas.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.vardadienas.data.daos.FavouriteNameDayDao
import com.example.vardadienas.data.daos.NameDayDao
import com.example.vardadienas.data.daos.NameDayExtendedDao
import com.example.vardadienas.data.entities.FavouriteNameDayReminder
import com.example.vardadienas.data.entities.NameDay
import com.example.vardadienas.data.entities.NameDayExtended
import com.example.vardadienas.data.valueClasses.MonthDay
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [NameDay::class, NameDayExtended::class, FavouriteNameDayReminder::class], version = 1)
@TypeConverters(Converters::class)
abstract class NameDayDatabase : RoomDatabase() {
    abstract fun nameDayDao(): NameDayDao
    abstract fun nameDayExtendedDao(): NameDayExtendedDao
    abstract fun favouriteNameDayDao(): FavouriteNameDayDao


    companion object {
        @Volatile
        private var INSTANCE: NameDayDatabase? = null

        fun getDatabase(context: Context): NameDayDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NameDayDatabase::class.java,
                    "name_day_database"
                )
                    .addCallback(createCallback(context)) // This is where the magic happens
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // The callback to populate the DB on first creation
        private fun createCallback(context: Context): Callback {
            return object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(context)
                    }
                }
            }
        }

        private suspend fun populateDatabase(context: Context) {
            val database = getDatabase(context)

            val gson = Gson()

            // --- Ingest the standard NameDay data ---
            try {
                // 1. PARSE the JSON into a Map
                val nameDaysJson = context.assets.open("name_day_data.json").bufferedReader().use { it.readText() }

                // Define the type for Gson: a Map with String keys and a List of Strings as values
                val mapType = object : TypeToken<Map<String, List<String>>>() {}.type

                // This map will look like: {"11-17" -> ["Hugo", "Uga", "Uģis"], "11-18" -> [...]}
                val nameDaysMap: Map<String, List<String>> = gson.fromJson(nameDaysJson, mapType)

                // 2. TRANSFORM and FLATTEN the Map into a List<NameDay>
                val nameDayListForDb = mutableListOf<NameDay>()
                var idCounter = 1 // To generate a unique primary key for each entry

                // Loop through each entry in the map (e.g., "11-17" and its list of names)
                for ((dateString, names) in nameDaysMap.entries) {
                    try {
                        // Manually create the MonthDay object from the date string key
                        val parts = dateString.split("-")
                        val month = parts[0].toInt()
                        val day = parts[1].toInt()
                        val monthDay = MonthDay(month * 100 + day)

                        // Loop through the list of names for that date
                        for (name in names) {
                            // Create a separate NameDay object for each name
                            nameDayListForDb.add(
                                NameDay(
                                    nameDayId = idCounter++,
                                    name = name,
                                    date = monthDay
                                )
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("PopulateDB", "Skipping malformed date: $dateString", e)
                    }
                }

                // 3. INSERT the final, flattened list into the database
                database.nameDayDao().insertAll(nameDayListForDb)
                Log.d("PopulateDB", "Successfully inserted ${nameDayListForDb.size} name days.")

            } catch (e: Exception) {
                Log.e("PopulateDB", "Error processing name_day_data.json", e)
            }

            // --- Repeat the same logic for your extended name days JSON ---
            try {
                val extendedJson = context.assets.open("name_day_data_extended.json").bufferedReader().use { it.readText() }
                val mapType = object : TypeToken<Map<String, List<String>>>() {}.type
                val extendedMap: Map<String, List<String>> = gson.fromJson(extendedJson, mapType)

                val extendedListForDb = mutableListOf<NameDayExtended>()
                var idCounter = 1 // Reset counter for the second table

                for ((dateString, names) in extendedMap.entries) {
                    // ... (same transformation logic as above) ...
                    val parts = dateString.split("-")
                    val month = parts[0].toInt()
                    val day = parts[1].toInt()
                    val monthDay = MonthDay(month * 100 + day)
                    for (name in names) {
                        extendedListForDb.add(
                            NameDayExtended(
                                nameDayId = idCounter++,
                                name = name,
                                date = monthDay
                            )
                        )
                    }
                }
                database.nameDayExtendedDao().insertAll(extendedListForDb)
                Log.d("PopulateDB", "Successfully inserted ${extendedListForDb.size} extended name days.")

            } catch (e: Exception) {
                Log.e("PopulateDB", "Error processing name_day_data_extended.json", e)
            }
        }
    }
}