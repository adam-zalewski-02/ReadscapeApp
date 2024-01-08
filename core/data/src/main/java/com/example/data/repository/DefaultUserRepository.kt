package com.example.data.repository

import com.example.network.ReadscapeNetworkDataSource
import com.example.network.model.AuthResponse
import com.example.network.model.NetworkUser
import com.example.network.model.catalog.CatalogPostResponse
import com.example.network.model.catalog.CatalogResponse
import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
    private val dataSource: ReadscapeNetworkDataSource
) : UserRepository {
    override suspend fun getUsers(): List<NetworkUser> {
        return dataSource.getUsers()
    }

    override suspend fun getUser(email:String, password: String): AuthResponse {
        return dataSource.getUser(email, password)
    }

    override suspend fun getUserEmail(userId: String): Result<String> {
        return dataSource.getUserEmail(userId)
    }



    override suspend fun addUser(email:String, password: String): AuthResponse {
        return dataSource.insertUser(email, password)
    }

    override fun updateUser(user: NetworkUser) {
        TODO("Not yet implemented")
    }

    override fun deleteUser(userId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getCollection(userId: String): CatalogResponse {
        return dataSource.getCollection(userId)
    }

    override suspend fun insertIntoCollection(userId: String, bookId: String): CatalogPostResponse {
        return dataSource.addToCollection(userId, bookId)
    }

}