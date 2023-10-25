package com.example.network

import com.example.network.model.NetworkUser

interface ReadscapeNetworkDataSource {
    suspend fun getUsers() : List<NetworkUser>
    suspend fun getUser(id: Int) : NetworkUser
    suspend fun insertUser(user: NetworkUser)
    suspend fun deleteUser(user: NetworkUser)
}