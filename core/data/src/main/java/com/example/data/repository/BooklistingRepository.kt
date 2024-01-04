package com.example.data.repository

import com.example.model.book.BookListing
import com.example.network.CmsNetworkDatasource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import com.example.common.network.Dispatcher
import com.example.common.network.ReadscapeDispatchers.IO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

interface BookListingRepository {
    fun getFilteredBookListings(start: Int, limit: Int, filters: Map<String, String>): Flow<List<BookListing>>
    suspend fun getSingleBookListing(listingId: String): BookListing
}

class BookListingRepositoryImpl @Inject constructor(
    private val cmsNetworkDatasource: CmsNetworkDatasource,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : BookListingRepository {

    override fun getFilteredBookListings(start: Int, limit: Int, filters: Map<String, String>): Flow<List<BookListing>> = flow {
        emit(cmsNetworkDatasource.getBookListings(start, limit, filters))
    }.flowOn(ioDispatcher)

    override suspend fun getSingleBookListing(listingId: String): BookListing {
        // Implement the logic to fetch details of a specific book listing based on the listingId.
        // This method will require a network call or database query to fetch a single book listing.
        TODO("Implement the method to get a specific book listing by ID")
    }
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideBookListingRepository(
        cmsNetworkDatasource: CmsNetworkDatasource,
        @Dispatcher(IO) ioDispatcher: CoroutineDispatcher
    ): BookListingRepository {
        return BookListingRepositoryImpl(cmsNetworkDatasource, ioDispatcher)
    }
}
