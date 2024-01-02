package com.example.network.retrofit

import com.example.model.book.BookListing
import com.example.network.BuildConfig
import com.example.network.CmsNetworkDatasource
import okhttp3.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

private const val BASE_URL = BuildConfig.CMS_URL

private interface RetrofitCmsNetworkApi {
    @GET(value = "/booklistings")
    suspend fun getBookListings(
        @Query("_start") start: Int,
        @Query("_limit") limit: Int
    ): List<BookListing>
}

@Singleton
class RetrofitCmsNetwork @Inject constructor(
    okhttpCallFactory: Call.Factory,
): CmsNetworkDatasource {
    private val retrofit = Retrofit.Builder()
        .callFactory(okhttpCallFactory)
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val cmsNetworkApi = retrofit.create(RetrofitCmsNetworkApi::class.java)

    override suspend fun getBookListings(start: Int, limit: Int): List<BookListing> {
        return cmsNetworkApi.getBookListings(start, limit)
    }

}