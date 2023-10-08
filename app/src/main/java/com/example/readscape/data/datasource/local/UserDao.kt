package com.example.readscape.data.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.readscape.data.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): List<User>

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: Int): User?
    @Query("SELECT * FROM users WHERE email = :email")
    fun getUserByEmail(email: String): User?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(vararg user: User)

    @Delete
    fun deleteUser(user: User)
}
