package com.example.network

import com.example.model.book.BookListing

interface CmsNetworkDatasource {
    suspend fun getBookListings(start: Int, limit: Int): List<BookListing>
}