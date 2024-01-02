package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.model.DarkThemeConfigEntity

@Dao
interface DarkThemeConfigDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDarkThemeConfig(darkThemeConfigEntity: DarkThemeConfigEntity)

    @Query("SELECT * FROM darkthemeconfig WHERE id = :darkThemeConfigId")
    suspend fun getDarkThemeConfig(darkThemeConfigId: Long): DarkThemeConfigEntity?
}