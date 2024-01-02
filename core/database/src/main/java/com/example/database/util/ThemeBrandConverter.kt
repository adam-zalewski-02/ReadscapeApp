package com.example.database.util

import androidx.room.TypeConverter
import com.example.model.ThemeBrand

class ThemeBrandConverter {
    @TypeConverter
    fun fromString(value: String): ThemeBrand = ThemeBrand.valueOf(value)

    @TypeConverter
    fun toString(themeBrand: ThemeBrand): String = themeBrand.name
}