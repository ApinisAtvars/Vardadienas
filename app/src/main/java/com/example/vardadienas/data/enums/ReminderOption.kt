package com.example.vardadienas.data.enums

import java.time.LocalDate

enum class ReminderOption(
    val userFriendlyName: String,
    private val calculation: (baseDate: LocalDate) -> LocalDate
) {
    DAY_OF("Vārda dienā", { it }), // 'it' is the baseDate, no change needed
    DAY_BEFORE("1 dienu pirms", { it.minusDays(1) }),
    TWO_DAYS_BEFORE("2 dienas pirms", { it.minusDays(2) }),
    WEEK_BEFORE("1 nedēļu pirms", { it.minusWeeks(1) });

    /**
     * Calculates the reminder date based on this option.
     * @param baseDate The original date (e.g., the name day).
     * @return The new, calculated LocalDate for the reminder.
     */
    fun calculateReminderDate(baseDate: LocalDate): LocalDate {
        return calculation(baseDate)
    }
}