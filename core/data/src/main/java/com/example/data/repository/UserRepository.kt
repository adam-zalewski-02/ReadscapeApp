package com.example.data.repository
import com.example.network.model.AuthResponse
import com.example.network.model.EmailResponse
import com.example.network.model.NetworkUser
import com.example.network.model.TransactionResponse
import com.example.network.model.catalog.CatalogPostResponse
import com.example.network.model.catalog.CatalogResponse

interface UserRepository {
    suspend fun getUsers(): List<NetworkUser>
    suspend fun getUser(email: String, password: String): AuthResponse?
    suspend fun getUserEmail(userId: String): Result<EmailResponse>
    suspend fun addUser(email: String, password: String): AuthResponse?
    fun updateUser(user: NetworkUser)
    fun deleteUser(userId: Int)

    suspend fun getCollection(userId: String): CatalogResponse
    suspend fun insertIntoCollection(userId: String, bookId: String): CatalogPostResponse

    suspend fun insertIntoTransactions(userId: String, toUserId: String, isbn: String, duration: Int): TransactionResponse
}