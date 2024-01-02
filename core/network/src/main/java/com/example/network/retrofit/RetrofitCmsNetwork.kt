package com.example.network.retrofit

import com.example.model.book.BookListing
import com.example.network.BuildConfig
import com.example.network.CmsNetworkDatasource
import okhttp3.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap
import javax.inject.Inject
import javax.inject.Singleton

private const val BASE_URL = BuildConfig.CMS_URL

private interface RetrofitCmsNetworkApi {
    @GET(value = "/booklistings")
    suspend fun getBookListings(
        @Query("_start") start: Int,
        @Query("_limit") limit: Int,
        @QueryMap filters: Map<String, String>
    ): List<BookListing>
}

@Singleton
class RetrofitCmsNetwork @Inject constructor(
    okhttpCallFactory: Call.Factory,
): CmsNetworkDatasource {

    private val genericRetrofitNetwork = GenericRetrofitNetwork(
        okhttpCallFactory,
        BASE_URL,
        RetrofitCmsNetworkApi::class.java
    )

    private val cmsNetworkApi = genericRetrofitNetwork.networkApi

    override suspend fun getBookListings(start: Int, limit: Int, filters: Map<String, String>): List<BookListing> {
        return cmsNetworkApi.getBookListings(start, limit, filters)
    }
}