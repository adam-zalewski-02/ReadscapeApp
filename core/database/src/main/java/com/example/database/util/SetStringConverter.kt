package com.example.database.util

import androidx.room.TypeConverter

class SetStringConverter {
    @TypeConverter
    fun fromString(value: String): Set<String> {
        return value.split(",").toSet()
    }

    @TypeConverter
    fun toString(value: Set<String>): String {
        return value.joinToString(",")
    }
}