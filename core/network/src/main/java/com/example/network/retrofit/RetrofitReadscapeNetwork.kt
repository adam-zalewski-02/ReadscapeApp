package com.example.network.retrofit
import com.example.network.BuildConfig
import com.example.network.ReadscapeNetworkDataSource
import com.example.network.model.AuthRequest
import com.example.network.model.AuthResponse
import com.example.network.model.EmailResponse
import com.example.network.model.NetworkUser
import com.example.network.model.TransactionRequest
import com.example.network.model.TransactionResponse
import com.example.network.model.TransactionsResponse
import com.example.network.model.catalog.CatalogPostResponse
import com.example.network.model.catalog.CatalogRequest
import com.example.network.model.catalog.CatalogResponse
import okhttp3.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

private const val BASE_URL = BuildConfig.BACKEND_URL

private interface RetrofitReadscapeNetworkApi {
    @GET(value = "/users")
    suspend fun getUsers(): List<NetworkUser>
    @GET(value = "/users/{id}")
    suspend fun getUser(@Path("id") id: Int): NetworkUser

    @GET("/users/{id}/email")
    suspend fun getUserEmail(@Path("id") id: String): Response<EmailResponse> // Wrap with Response for error handling

    @GET(value = "/collections/{userId}")
    suspend fun getCollection(@Path("userId") userId: String) : CatalogResponse

    @POST(value = "/auth/login")
    suspend fun login(@Body request: AuthRequest): AuthResponse
    @POST(value = "/auth/register")
    suspend fun register(@Body request: AuthRequest): AuthResponse
    @POST(value = "/collections/{userId}")
    suspend fun insertIntoCollection(@Path("userId") userId: String, @Body request: CatalogRequest) : CatalogPostResponse

    @POST(value = "/transactions/lend")
    suspend fun insertIntoTransactions(@Body request: TransactionRequest) : TransactionResponse

    @GET("/transactions/user/{userId}")
    suspend fun getUserTransactions(@Path("userId") userId: String): Response<TransactionsResponse>

    @DELETE(value = "/users/{id}")
    suspend fun deleteUser(@Path("id") id: Int)
}

@Singleton
class RetrofitReadscapeNetwork @Inject constructor(
    okhttpCallFactory: Call.Factory,
): ReadscapeNetworkDataSource {

    private val genericRetrofitNetwork = GenericRetrofitNetwork(
        okhttpCallFactory,
        BASE_URL,
        RetrofitReadscapeNetworkApi::class.java
    )
    override suspend fun getUsers(): List<NetworkUser> {
        return genericRetrofitNetwork.networkApi.getUsers()
    }

    override suspend fun getUser(email: String, password: String): AuthResponse {
        val loginRequest = AuthRequest(
            email = email,
            password = password
        )
        return genericRetrofitNetwork.networkApi.login(loginRequest)
    }

    override suspend fun getUserEmail(userId: String): Result<EmailResponse> {
        return try {
            val response = genericRetrofitNetwork.networkApi.getUserEmail(userId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(RuntimeException("Failed to fetch email: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun insertUser(email: String, password: String): AuthResponse {
        val registerRequest = AuthRequest(
            email = email,
            password = password
        )
        return genericRetrofitNetwork.networkApi.register(registerRequest)
    }

    override suspend fun deleteUser(user: NetworkUser) {
        genericRetrofitNetwork.networkApi.deleteUser(1)
    }

    override suspend fun getCollection(userId: String): CatalogResponse {
        return genericRetrofitNetwork.networkApi.getCollection(userId)
    }

    override suspend fun addToCollection(userId: String, bookId: String): CatalogPostResponse {
        val postRequest = CatalogRequest(
            bookId = bookId
        )
        return genericRetrofitNetwork.networkApi.insertIntoCollection(userId, postRequest)
    }

    override suspend fun insertIntoTransactions(userId: String, toUserId: String, isbn: String, duration: Int): TransactionResponse {
        val transactionRequest = TransactionRequest(
            userId = userId,
            toUserId = toUserId,
            isbn = isbn,
            duration = duration
        )
        return genericRetrofitNetwork.networkApi.insertIntoTransactions(transactionRequest)
    }

    override suspend fun getUserTransactions(userId: String): Result<TransactionsResponse> {
        return try {
            val response = genericRetrofitNetwork.networkApi.getUserTransactions(userId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(RuntimeException("Failed to fetch transactions: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}