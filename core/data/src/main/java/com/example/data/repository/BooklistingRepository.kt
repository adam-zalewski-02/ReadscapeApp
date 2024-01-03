package com.example.data.repository

import com.example.model.book.BookListing
import com.example.network.CmsNetworkDatasource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface BookListingRepository {
    fun getFilteredBookListings(start: Int, limit: Int, filters: Map<String, String>): Flow<List<BookListing>>
    suspend fun getSingleBookListing(listingId: String): BookListing
}

class BookListingRepositoryImpl @Inject constructor(
    private val cmsNetworkDatasource: CmsNetworkDatasource
) : BookListingRepository {

    override fun getFilteredBookListings(start: Int, limit: Int, filters: Map<String, String>): Flow<List<BookListing>> = flow {
        emit(cmsNetworkDatasource.getBookListings(start, limit, filters))
    }

    override suspend fun getSingleBookListing(listingId: String): BookListing {
        // Implement the logic to fetch details of a specific book listing based on the listingId.
        // This method will require a network call or database query to fetch a single book listing.
        TODO("Implement the method to get a specific book listing by ID")
    }
}