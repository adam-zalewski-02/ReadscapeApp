package com.example.data.repository
import com.example.network.model.NetworkUser

interface UserRepository {
    suspend fun getUsers(): List<NetworkUser>
    suspend fun getUser(email: String, password: String): NetworkUser?
    suspend fun addUser(user: NetworkUser)
    fun updateUser(user: NetworkUser)
    fun deleteUser(userId: Int)
}