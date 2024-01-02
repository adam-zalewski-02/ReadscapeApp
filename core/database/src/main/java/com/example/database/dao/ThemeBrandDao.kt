package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.model.ThemeBrandEntity

@Dao
interface ThemeBrandDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThemeBrand(themeBrandEntity: ThemeBrandEntity)

    @Query("SELECT * FROM themebrand WHERE id = :themeBrandId")
    suspend fun getThemeBrand(themeBrandId: Long): ThemeBrandEntity?
}