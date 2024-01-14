package com.example.network

import com.example.network.model.AuthResponse
import com.example.network.model.EmailResponse
import com.example.network.model.NetworkUser
import com.example.network.model.TransactionResponse
import com.example.network.model.TransactionsResponse
import com.example.network.model.catalog.CatalogPostResponse
import com.example.network.model.catalog.CatalogResponse

interface ReadscapeNetworkDataSource {
    suspend fun getUsers() : List<NetworkUser>
    suspend fun getUser(email: String, password: String) : AuthResponse
    suspend fun getUserEmail(userId: String): Result<EmailResponse>
    suspend fun insertUser(email: String, password: String) : AuthResponse
    suspend fun deleteUser(user: NetworkUser)
    suspend fun getUserTransactions(userId: String): Result<TransactionsResponse>
    suspend fun getCollection(userId: String) : CatalogResponse
    suspend fun addToCollection(userId: String, bookId: String) : CatalogPostResponse

    suspend fun insertIntoTransactions(userId: String, toUserId: String, isbn: String, duration: Int) : TransactionResponse
    suspend fun getSensorKit(kitId: String): Result<SensorKitResponse>
}