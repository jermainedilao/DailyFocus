package com.jermaine.dailyfocus.util

import java.time.format.DateTimeFormatter

val DATETIME_FORMATTER_DAY_MONTH_YEAR: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd MMM yyyy")
val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("h:mm a")
