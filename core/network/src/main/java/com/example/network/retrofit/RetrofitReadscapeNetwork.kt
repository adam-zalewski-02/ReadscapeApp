package com.example.network.retrofit
import com.example.network.ReadscapeNetworkDataSource
import com.example.network.model.NetworkUser
import kotlinx.serialization.json.Json
import okhttp3.Call
import javax.inject.Inject
import javax.inject.Singleton

private const val BASE_URL = ""
private interface RetrofitReadscapeNetworkApi {

}
@Singleton
class RetrofitReadscapeNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: Call.Factory,
): ReadscapeNetworkDataSource {
    override suspend fun getUsers(): List<NetworkUser> {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(id: Int): NetworkUser {
        TODO("Not yet implemented")
    }

    override suspend fun insertUser(user: NetworkUser) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(user: NetworkUser) {
        TODO("Not yet implemented")
    }

}