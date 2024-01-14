package com.example.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.database.util.DarkThemeConfigConverter
import com.example.model.DarkThemeConfig

@Entity(tableName = "darkthemeconfig")
data class DarkThemeConfigEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @TypeConverters(DarkThemeConfigConverter::class)
    val darkThemeConfig: DarkThemeConfig
)
fun DarkThemeConfigEntity.asExternalModel() = darkThemeConfig
