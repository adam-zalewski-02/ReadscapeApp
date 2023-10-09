package com.example.data.repository
import com.example.model.User
interface UserRepository {
    fun getUsers(): List<User>
    fun getUserById(userId: Int): User?
    fun addUser(user: User)
    fun updateUser(user: User)
    fun deleteUser(userId: Int)
}