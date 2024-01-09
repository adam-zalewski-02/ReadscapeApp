package com.example.network

import com.example.model.book.BookListing

interface CmsNetworkDatasource {
    suspend fun getBookListings(start: Int, limit: Int, filters: Map<String, String>): List<BookListing>
    suspend fun getSingleBookListing(listingId: String): BookListing?
    suspend fun addBookListing(bookListing: BookListing): BookListing
    suspend fun updateBookListingByIsbn(isbn: String, updatedBookListing: BookListing): BookListing?
}