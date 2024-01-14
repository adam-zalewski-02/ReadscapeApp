package com.example.database.util

import androidx.room.TypeConverter
import com.example.model.DarkThemeConfig

class DarkThemeConfigConverter {
    @TypeConverter
    fun fromString(value: String): DarkThemeConfig = DarkThemeConfig.valueOf(value)

    @TypeConverter
    fun toString(darkThemeConfig: DarkThemeConfig): String = darkThemeConfig.name
}