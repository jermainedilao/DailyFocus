package com.jermaine.dailyfocus.data.local

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime

class Converters {
    @TypeConverter
    fun toLocalTime(secondOfDay: Long): LocalTime {
        return LocalTime.ofSecondOfDay(secondOfDay)
    }

    @TypeConverter
    fun toLong(localTime: LocalTime): Long {
        return localTime.toSecondOfDay().toLong()
    }

    @TypeConverter
    fun toLocalDate(epochDay: Long): LocalDate {
        return LocalDate.ofEpochDay(epochDay)
    }

    @TypeConverter
    fun toLong(localDate: LocalDate): Long {
        return localDate.toEpochDay()
    }
}
