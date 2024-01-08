package com.example.network.retrofit

import com.example.model.book.Volume
import com.example.network.GoogleNetworkDataSource
import com.example.network.model.book.BookApiResponse
import okhttp3.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

private const val BASE_URL = "https://www.googleapis.com/books/v1/"

private interface RetrofitGoogleNetworkApi {
    @GET(value = "volumes?q=search+terms")
    suspend fun getAllVolumes() : BookApiResponse

    @GET(value = "volumes/{id}")
    suspend fun getVolumeById(@Path("id") id: String) : Volume
    @GET("volumes")
    suspend fun getVolumesByTitle(@Query("q") title: String): BookApiResponse
}
@Singleton
class RetrofitGoogleNetwork @Inject constructor(
    okhttpCallFactory: Call.Factory,
) : GoogleNetworkDataSource {

    private val genericRetrofitNetwork = GenericRetrofitNetwork(
        okhttpCallFactory,
        BASE_URL,
        RetrofitGoogleNetworkApi::class.java
    )

    override suspend fun getAllVollumes(): List<Volume> {
        return genericRetrofitNetwork.networkApi.getAllVolumes().items
    }

    override suspend fun getVolumeById(volumeId: String): Volume {
        return genericRetrofitNetwork.networkApi.getVolumeById(volumeId)
    }

    override suspend fun getVolumesByTitle(title: String): List<Volume> {
        return genericRetrofitNetwork.networkApi.getVolumesByTitle(title).items
    }
}