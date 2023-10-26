package com.example.network.retrofit
import com.example.network.ReadscapeNetworkDataSource
import com.example.network.model.NetworkUser
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

private const val BASE_URL = "http://localhost:3000/"
private interface RetrofitReadscapeNetworkApi {
    @GET(value = "")
    suspend fun getUsers(): NetworkResponse<List<NetworkUser>>
    @GET(value = "{id}")
    suspend fun getUser(@Path("id") id: Int): NetworkResponse<NetworkUser>

    @POST(value = "register")
    suspend fun register(@Body user: NetworkUser): NetworkResponse<Unit>

    @DELETE(value = "{id}")
    suspend fun deleteUser(@Path("id") id: Int): NetworkResponse<Unit>
}

@Serializable
private data class NetworkResponse<T> (
    val data: T
)

@Singleton
class RetrofitReadscapeNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: Call.Factory,
): ReadscapeNetworkDataSource {

    private val networkApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(RetrofitReadscapeNetworkApi::class.java)
    override suspend fun getUsers(): List<NetworkUser> {
        return networkApi.getUsers().data
    }

    override suspend fun getUser(id: Int): NetworkUser {
        return networkApi.getUser(id).data
    }

    override suspend fun insertUser(user: NetworkUser) {
        networkApi.register(user)
    }

    override suspend fun deleteUser(user: NetworkUser) {
        networkApi.deleteUser(user.id)
    }

}