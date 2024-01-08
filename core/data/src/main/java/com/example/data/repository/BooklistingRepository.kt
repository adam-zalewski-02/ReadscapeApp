package com.example.data.repository

import android.util.Log
import com.example.model.book.BookListing
import com.example.network.CmsNetworkDatasource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import com.example.common.network.Dispatcher
import com.example.common.network.ReadscapeDispatchers.IO
import com.example.network.GoogleNetworkDataSource
import com.example.network.ReadscapeNetworkDataSource
import com.example.network.model.EmailResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

interface BookListingRepository {
    fun getFilteredBookListings(start: Int, limit: Int, filters: Map<String, String>): Flow<List<BookListing>>
    suspend fun getSingleBookListing(listingId: String): BookListing?
    suspend fun getOwnerEmailById(ownerId: String): Result<String>
    suspend fun addBookListing(bookListing: BookListing): Result<BookListing>
    suspend fun addBookListingWithGoogleData(bookListing: BookListing): Result<BookListing>
    suspend fun updateBookListingByIsbn(isbn: String, updatedBookListing: BookListing): Result<BookListing>
}

class BookListingRepositoryImpl @Inject constructor(
    private val cmsNetworkDatasource: CmsNetworkDatasource,
    private val googleNetworkDataSource: GoogleNetworkDataSource,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) : BookListingRepository {

    override fun getFilteredBookListings(start: Int, limit: Int, filters: Map<String, String>): Flow<List<BookListing>> = flow {
        emit(cmsNetworkDatasource.getBookListings(start, limit, filters))
    }.flowOn(ioDispatcher)

    override suspend fun getSingleBookListing(listingId: String): BookListing? {
        return try {
            cmsNetworkDatasource.getSingleBookListing(listingId)
        } catch (e: Exception) {
            Log.d("BookListingRepositoryImpl", "No booklisting found with id $listingId")
            null
        }
    }

    override suspend fun getOwnerEmailById(ownerId: String): Result<String> {
        return try {
            testAddBookListingWithGoogleData()
            val result = userRepository.getUserEmail(ownerId)
            if (result.isSuccess) {
                Result.success(result.getOrNull()?.email ?: throw IllegalStateException("Email not found"))
            } else {
                result as Result<String>
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addBookListing(bookListing: BookListing): Result<BookListing> {
        return try {
            val result = cmsNetworkDatasource.addBookListing(bookListing)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addBookListingWithGoogleData(bookListing: BookListing): Result<BookListing> {
        return try {

            Log.d("BookRepo", "Fetching details from Google Books API for: ${bookListing.title}")
            // Fetch details from Google Books API
            val volumes = googleNetworkDataSource.getVolumesByTitle(bookListing.title)
            val googleVolumeInfo = volumes.firstOrNull()?.volumeInfo

            // Merge Google Books data with the bookListing object
            googleVolumeInfo?.let {
                bookListing.pageCount = it.pageCount ?: bookListing.pageCount
                bookListing.thumbnailLink = it.imageLinks?.thumbnail ?: bookListing.thumbnailLink
                bookListing.authors = it.authors ?: bookListing.authors
                bookListing.language = it.language ?: bookListing.language
                bookListing.publisher = it.publisher ?: bookListing.publisher
                bookListing.categories = it.categories ?: bookListing.categories
                bookListing.publishedDate = it.publishedDate ?: bookListing.publishedDate
                bookListing.description = it.description ?: bookListing.description
                bookListing.title = it.title
                bookListing.id = it.id ?: bookListing.id
            }
            Log.d("BookRepo", "Adding book listing to Strapi")
            // Add the book listing
            val result = cmsNetworkDatasource.addBookListing(bookListing)
            Log.d("BookRepo", "Book listing added successfully: ${result._id}")
            Result.success(result)
        } catch (e: Exception) {
            Log.e("BookRepo", "Error in addBookListingWithGoogleData", e)
            Result.failure(e)
        }
    }

    suspend fun testAddBookListingWithGoogleData() {
        val dummyBookListing = BookListing(
            pageCount = 0,
            thumbnailLink = "",
            canBeBorrowed = true,
            canBeSold = true,
            authors = emptyList(),
            keywords = emptyList(),
            extraInfoFromOwner = "",
            maturityRating = "",
            ownerId = "65290e4e3277e881354a4d15",
            isbn = "isbn_placeholder",
            title = "The Lord of the Rings",
            language = "",
            publisher = "",
            categories = emptyList(),
            publishedDate = "",
            description = "",
            similarBooks = emptyList(),
            createdAt = "",
            updatedAt = "",
            __v = 0,
            published_at = null,
            id = ""
        )

        addBookListingWithGoogleData(dummyBookListing)
    }

    override suspend fun updateBookListingByIsbn(isbn: String, updatedBookListing: BookListing): Result<BookListing> {
        return try {
            val result = cmsNetworkDatasource.updateBookListingByIsbn(isbn, updatedBookListing)
            result?.let { Result.success(it) } ?: Result.failure(Exception("Book listing not found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideBookListingRepository(
        cmsNetworkDatasource: CmsNetworkDatasource,
        googleNetworkDataSource: GoogleNetworkDataSource,
        readscapeNetworkDataSource: ReadscapeNetworkDataSource,
        @Dispatcher(IO) ioDispatcher: CoroutineDispatcher
    ): BookListingRepository {
        return BookListingRepositoryImpl(cmsNetworkDatasource, googleNetworkDataSource, ioDispatcher, DefaultUserRepository(readscapeNetworkDataSource))
    }
}
