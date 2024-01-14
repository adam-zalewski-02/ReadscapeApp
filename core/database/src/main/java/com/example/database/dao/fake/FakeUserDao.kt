package com.example.database.dao.fake

import com.example.database.dao.UserDao
import com.example.database.model.UserEntity

class FakeUserDao : UserDao {
    private val users = mutableListOf<UserEntity>()

    override fun getAllUsers(): List<UserEntity> {
        return users.toList()
    }

    override fun getUserById(userId: Int): UserEntity? {
        return users.find { it.id == userId }
    }

    override fun getUserByEmail(email: String): UserEntity? {
        return users.find { it.email == email }
    }

    override fun insertUsers(vararg user: UserEntity) {
        users.addAll(user)
    }

    override fun deleteUser(user: UserEntity) {
        users.remove(user)
    }
}