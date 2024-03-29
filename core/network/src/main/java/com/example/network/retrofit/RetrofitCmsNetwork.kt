package com.example.network.retrofit

import com.example.model.CurrentUserManager
import com.example.model.book.BookListing
import com.example.network.BuildConfig
import com.example.network.CmsNetworkDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
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
        @QueryMap(encoded = true) filters: Map<String, String>
    ): List<BookListing>

    @GET(value = "/booklistings/{id}")
    suspend fun getSingleBookListing(@Path("id") listingId: String): BookListing?

    @POST("/booklistings")
    suspend fun addBookListing(@Body bookListing: BookListing): BookListing

    @PUT("/booklistings/{id}")
    suspend fun updateBookListing(@Path("id") listingId: String, @Body bookListing: BookListing): BookListing

    @DELETE("/booklistings/{id}")
    suspend fun deleteBookListingById(@Path("id") listingId: String): Response<Unit>

    @GET("/booklistings")
    suspend fun getBookListingByISBNAndOwner(
        @Query("isbn") isbn: String,
        @Query("ownerId") ownerId: String,
        @Query("_publicationState") publicationState: String
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
    // Use this method to get a list of BookListings with applied filters.
    // The filters parameter is a map where each key-value pair represents a query parameter and its value.
    // For example, to filter by author and language, you would pass:
    // val filters = mapOf("author" to "John Doe", "language" to "English")
    // Then call this method with the filters map:
    // getBookListings(start = 0, limit = 10, filters = filters)
    override suspend fun getBookListings(start: Int, limit: Int, filters: Map<String, String>): List<BookListing> {
        val modifiedFilters = filters.mapKeys { (key, _) -> "${key}_contains" }
        return cmsNetworkApi.getBookListings(start, limit, modifiedFilters)
    }

    override suspend fun getSingleBookListing(listingId: String): BookListing? {
        return cmsNetworkApi.getSingleBookListing(listingId)
    }

    override suspend fun getSingleBookListingByIsbnForCurrentUser(isbn: String): BookListing? {
        val currentUser = CurrentUserManager.getCurrentUser()
        val ownerId = currentUser?.userId ?: return null

        val liveListings = cmsNetworkApi.getBookListingByISBNAndOwner(isbn, ownerId, "live")

        val draftListings = cmsNetworkApi.getBookListingByISBNAndOwner(isbn, ownerId, "preview")

        return (liveListings + draftListings).firstOrNull()
    }

    override suspend fun addBookListing(bookListing: BookListing): BookListing {
        return cmsNetworkApi.addBookListing(bookListing)
    }

    override suspend fun updateBookListingByIsbn(isbn: String, updatedBookListing: BookListing): BookListing? {
        val bookListingToUpdate = getSingleBookListingByIsbnForCurrentUser(isbn)
        return bookListingToUpdate?.let {
            cmsNetworkApi.updateBookListing(it._id ?: return null, updatedBookListing)
        }
    }

    override suspend fun deleteBookListingByIsbnAndOwner(isbn: String): Response<Unit> {
        val bookListingToDelete = getSingleBookListingByIsbnForCurrentUser(isbn)
        return bookListingToDelete?._id?.let { listingId ->
            cmsNetworkApi.deleteBookListingById(listingId)
        } ?: throw NoSuchElementException("No book listing found with ISBN: $isbn for current user.")
    }
}
