package com.example.expense_tracker.adapters


import android.util.Log
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class LocalDateJsonAdapter {

        val formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        // Serialize the LocalDate to String
        @ToJson
        fun toJson(date: LocalDate): String {
                return date.format(formatter)

        }

        // Parse the String back to a LocalDate
        @FromJson
        fun fromJson(date: String): LocalDate {
            return LocalDate.parse(date, formatter)
        }

}
