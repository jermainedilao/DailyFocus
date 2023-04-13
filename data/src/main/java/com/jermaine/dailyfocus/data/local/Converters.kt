package com.jermaine.dailyfocus.data.local

import androidx.room.TypeConverter
import java.time.LocalTime

class Converters {
    @TypeConverter
    fun toLocalTime(secondOfDay: Long): LocalTime {
        return LocalTime.ofSecondOfDay(secondOfDay)
    }

    @TypeConverter
    fun toInt(localTime: LocalTime): Long {
        return localTime.toSecondOfDay().toLong()
    }
}