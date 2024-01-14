package com.example.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.database.util.DarkThemeConfigConverter
import com.example.database.util.SetStringConverter
import com.example.database.util.ThemeBrandConverter
import com.example.model.DarkThemeConfig
import com.example.model.ThemeBrand
import com.example.model.UserData

@Entity(tableName = "userdata")
data class UserDataEntity(
    @PrimaryKey
    val id: String,
    @TypeConverters(SetStringConverter::class)
    val followedVolumes: Set<String>,
    @TypeConverters(ThemeBrandConverter::class)
    val themeBrand: ThemeBrand,
    @TypeConverters(DarkThemeConfigConverter::class)
    val darkThemeConfig: DarkThemeConfig,
    val useDynamicColor: Boolean,
    val shouldHideOnboarding: Boolean
)

fun UserDataEntity.asExternalModel() = UserData(
    followedVolumes = followedVolumes,
    themeBrand = themeBrand,
    darkThemeConfig = darkThemeConfig,
    useDynamicColor = useDynamicColor,
    shouldHideOnboarding = shouldHideOnboarding
)