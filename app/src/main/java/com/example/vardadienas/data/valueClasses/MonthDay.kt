package com.example.vardadienas.data.valueClasses

@JvmInline
value class MonthDay(
    // A sortable integer representation, e.g., March 15th is 315
    val value: Int
) {
    // Convenience properties to get the month and day back
    val month: Int
        get() = value / 100

    val day: Int
        get() = value % 100

    override fun toString(): String {
        // Re-format back to MM-DD for display if needed
        return String.format("%02d-%02d", month, day)
    }

    companion object {
        fun fromString(dateString: String): MonthDay {
            try {
                val parts = dateString.split("-")
                val month = parts[0].toInt()
                val day = parts[1].toInt()
                return MonthDay(month * 100 + day)
            } catch (e: Exception) {
                // Catch any NumberFormatException or IndexOutOfBoundsException
                throw IllegalArgumentException(
                    "Invalid date format. Expected 'MM-DD', but got '$dateString'.",
                    e
                )
            }
        }
    }
}