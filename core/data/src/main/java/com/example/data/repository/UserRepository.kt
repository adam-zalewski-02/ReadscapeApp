package com.example.data.repository
import com.example.network.model.AuthResponse
import com.example.network.model.NetworkUser

interface UserRepository {
    suspend fun getUsers(): List<NetworkUser>
    suspend fun getUser(email: String, password: String): AuthResponse?
    suspend fun addUser(email: String, password: String): AuthResponse?
    fun updateUser(user: NetworkUser)
    fun deleteUser(userId: Int)
}