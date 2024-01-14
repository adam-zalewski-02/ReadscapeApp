package com.example.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.database.util.ThemeBrandConverter
import com.example.model.ThemeBrand

@Entity(tableName = "themebrand")
data class ThemeBrandEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @TypeConverters(ThemeBrandConverter::class)
    val themeBrand: ThemeBrand
)
fun ThemeBrandEntity.asExternallModel() = themeBrand