package com.example.readscape.data.repository

import androidx.lifecycle.LiveData
import com.example.readscape.data.datasource.local.UserDao
import com.example.readscape.data.model.User

class UserRepository(private val userDao: UserDao) {

    fun getAllUsers(): List<User> {
        return userDao.getAllUsers();
    }
    fun getUserById(userId: Int): User? {
        return userDao.getUserById(userId)
    }

    fun getUserByEmail(email: String): User? {
        return  userDao.getUserByEmail(email)
    }

    fun insertUsers(vararg user: User) {
        userDao.insertUsers(*user)
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }
}