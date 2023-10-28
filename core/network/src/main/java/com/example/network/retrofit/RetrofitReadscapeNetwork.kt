package com.example.network.retrofit
import com.example.network.BuildConfig
import com.example.network.ReadscapeNetworkDataSource
import com.example.network.model.LoginResponse
import com.example.network.model.LoginRequest
import com.example.network.model.NetworkUser
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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

    @POST(value = "/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
    @POST(value = "/auth/register")
    suspend fun register(@Body user: NetworkUser)

    @DELETE(value = "/users/{id}")
    suspend fun deleteUser(@Path("id") id: Int)
}

@Singleton
class RetrofitReadscapeNetwork @Inject constructor(
    okhttpCallFactory: Call.Factory,
): ReadscapeNetworkDataSource {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val networkApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(RetrofitReadscapeNetworkApi::class.java)
    override suspend fun getUsers(): List<NetworkUser> {
        return networkApi.getUsers()
    }

    override suspend fun getUser(email: String, password: String): LoginResponse {
        val request = LoginRequest(
            email = email,
            password = password
        )
        return networkApi.login(request)
    }

    override suspend fun insertUser(user: NetworkUser) {
        networkApi.register(user)
    }

    override suspend fun deleteUser(user: NetworkUser) {
        networkApi.deleteUser(1)
    }

}