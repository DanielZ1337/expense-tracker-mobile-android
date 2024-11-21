package com.example.expense_tracker.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.Instant
import java.time.format.DateTimeParseException

class InstantAdapter {
    @FromJson
    fun fromJson(json: String): Instant {
        return try {
            Instant.parse(json)
        } catch (e: DateTimeParseException) {
            throw com.squareup.moshi.JsonDataException("Invalid date format: $json", e)
        }
    }

    @ToJson
    fun toJson(value: Instant): String {
        return value.toString()
    }
}

