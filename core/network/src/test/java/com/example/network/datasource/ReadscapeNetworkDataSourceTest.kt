package com.example.network.datasource

import com.example.model.Transaction
import com.example.network.ReadscapeNetworkDataSource
import com.example.network.model.AuthResponse
import com.example.network.model.EmailResponse
import com.example.network.model.NetworkUser
import com.example.network.model.TransactionResponse
import com.example.network.model.catalog.CatalogPostResponse
import com.example.network.model.catalog.CatalogResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ReadscapeNetworkDataSourceTest {

    private lateinit var readscapeNetworkDataSource: ReadscapeNetworkDataSource

    @Before
    fun setup() {
        readscapeNetworkDataSource = object : ReadscapeNetworkDataSource {
            override suspend fun getUsers(): List<NetworkUser> {
                // Mock implementation, return a dummy list for testing
                return listOf(
                    NetworkUser("1", "User1", "Password", 22),
                    NetworkUser("2", "User2","Password", 22)
                )
            }

            override suspend fun getUser(email: String, password: String): AuthResponse {
                // Mock implementation, return a dummy AuthResponse for testing
                return AuthResponse(success = true, user = NetworkUser("1", "User1", "Password", 22))
            }

            override suspend fun getUserEmail(userId: String): Result<EmailResponse> {
                // Mock implementation, return a dummy Result<EmailResponse> for testing
                return Result.success(EmailResponse("user@example.com"))
            }

            override suspend fun insertUser(email: String, password: String): AuthResponse {
                // Mock implementation, return a dummy AuthResponse for testing
                return AuthResponse(success = true, user = NetworkUser("1", "User1", "Password", 22))
            }

            override suspend fun deleteUser(user: NetworkUser) {
                // Mock implementation, do nothing for testing
            }

            override suspend fun getCollection(userId: String): CatalogResponse {
                // Mock implementation, return a dummy CatalogResponse for testing
                return CatalogResponse(_id = "1", user_id = userId, data = listOf("Book1", "Book2"))
            }

            override suspend fun addToCollection(userId: String, bookId: String): CatalogPostResponse {
                // Mock implementation, return a dummy CatalogPostResponse for testing
                return CatalogPostResponse(true, "Success")
            }

            override suspend fun insertIntoTransactions(
                userId: String,
                toUserId: String,
                isbn: String,
                duration: Int
            ): TransactionResponse {
                // Mock implementation, return a dummy TransactionResponse for testing
                return TransactionResponse(
                    success = true,
                    transaction = Transaction(
                        fromUser = userId,
                        toUser = toUserId,
                        isbn = isbn,
                        transactionType = "lend",
                        lendDate = "2022-01-01", // Replace with a valid date
                        duration = duration,
                        _id = "1"
                    )
                )
            }
        }
    }

    @Test
    fun `getUsers should return a list of NetworkUser`() = runBlocking {
        // Call the function and assert the result
        val result = readscapeNetworkDataSource.getUsers()
        val expected = listOf(
            NetworkUser("1", "User1", "Password", 22),
            NetworkUser("2", "User2", "Password", 22)
        )

        assertEquals(expected, result)
    }

    @Test
    fun `getUser should return an AuthResponse`() = runBlocking {
        // Call the function and assert the result
        val result = readscapeNetworkDataSource.getUser("user@example.com", "password")
        val expected = AuthResponse(success = true, user = NetworkUser("1", "User1", "Password", 22))

        assertEquals(expected, result)
    }

    @Test
    fun `getUserEmail should return a Result containing an EmailResponse`() = runBlocking {
        // Call the function and assert the result
        val result = readscapeNetworkDataSource.getUserEmail("1")
        val expected = Result.success(EmailResponse("user@example.com"))

        assertEquals(expected, result)
    }
    @Test
    fun `getCollection should return a CatalogResponse`() = runBlocking {
        // Call the function and assert the result
        val result = readscapeNetworkDataSource.getCollection("1")
        val expected = CatalogResponse(_id = "1", user_id = "1", data = listOf("Book1", "Book2"))

        assertEquals(expected, result)
    }

    @Test
    fun `insertIntoTransactions should return a TransactionResponse`() = runBlocking {
        // Call the function and assert the result
        val result = readscapeNetworkDataSource.insertIntoTransactions("1", "2", "ISBN123", 7)
        val expected = TransactionResponse(
            success = true,
            transaction = Transaction(
                fromUser = "1",
                toUser = "2",
                isbn = "ISBN123",
                transactionType = "lend",
                lendDate = "2022-01-01", // Replace with a valid date
                duration = 7,
                _id = "1"
            )
        )

        assertEquals(expected, result)
    }


    // Add more tests for other methods in ReadscapeNetworkDataSource interface
}