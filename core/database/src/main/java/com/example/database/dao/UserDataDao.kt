package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.model.UserDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserData(userDataEntity: UserDataEntity)

    @Query("SELECT * FROM userdata WHERE id = :userId")
    suspend fun getUserData(userId: String): UserDataEntity?
    @Query("SELECT * FROM userdata WHERE id = :userId")
    fun getUserDataFlow(userId: String): Flow<UserDataEntity?>
}