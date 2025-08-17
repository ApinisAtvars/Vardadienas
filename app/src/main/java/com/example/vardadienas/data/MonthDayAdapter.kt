package com.example.vardadienas.data

import com.example.vardadienas.data.valueClasses.MonthDay
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException

class MonthDayAdapter : TypeAdapter<MonthDay>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: MonthDay?) {
        if (value == null) {
            out.nullValue()
            return
        }
        // Write it back as a string if you ever serialize to JSON
        out.value(value.toString())
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): MonthDay? {
        if (reader.peek() == com.google.gson.stream.JsonToken.NULL) {
            reader.nextNull()
            return null
        }
        val dateString = reader.nextString() // e.g., "03-15"
        return try {
            val parts = dateString.split("-")
            val month = parts[0].toInt()
            val day = parts[1].toInt()
            // Convert to our sortable integer format
            MonthDay(month * 100 + day)
        } catch (e: Exception) {
            // Handle malformed date strings gracefully
            null
        }
    }
}