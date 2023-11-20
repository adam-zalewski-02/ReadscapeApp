package com.example.network

import com.example.network.model.AuthResponse
import com.example.network.model.NetworkUser

interface ReadscapeNetworkDataSource {
    suspend fun getUsers() : List<NetworkUser>
    suspend fun getUser(email: String, password: String) : AuthResponse
    suspend fun insertUser(email: String, password: String) : AuthResponse
    suspend fun deleteUser(user: NetworkUser)
}