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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

interface BookListingRepository {
    fun getFilteredBookListings(start: Int, limit: Int, filters: Map<String, String>): Flow<List<BookListing>>
    suspend fun getSingleBookListing(listingId: String): BookListing?
    suspend fun getSingleBookListingByIsbnForCurrentUser(isbn: String): BookListing?
    suspend fun getOwnerEmailById(ownerId: String): Result<String>
    suspend fun addBookListing(bookListing: BookListing): Result<BookListing>
    suspend fun addBookListingWithGoogleData(bookListing: BookListing): Result<BookListing>
    suspend fun updateBookListingByIsbn(isbn: String, updatedBookListing: BookListing): Result<BookListing>
    suspend fun deleteBookListingByIsbnAndOwner(isbn: String): Result<Unit>
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

    override suspend fun getSingleBookListingByIsbnForCurrentUser(isbn: String): BookListing? {
        return try {
            cmsNetworkDatasource.getSingleBookListingByIsbnForCurrentUser(isbn)
        } catch (e: Exception) {
            Log.d("BookListingRepositoryImpl", "No booklisting found with isbn $isbn")
            null
        }
    }

    override suspend fun getOwnerEmailById(ownerId: String): Result<String> {
        return try {
            //testAddBookListingWithGoogleData()
            //testUpdateBookListingWithIsbn()
            //testDeleteBookListingByIsbnAndOwner()
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
            val volumes = googleNetworkDataSource.getVolumesByIsbn(bookListing.isbn)
            val googleVolumeInfo = volumes.firstOrNull()?.volumeInfo

            googleVolumeInfo?.let {
                bookListing.pageCount = it.pageCount ?: bookListing.pageCount
                bookListing.thumbnailLink = it.imageLinks?.thumbnail ?: bookListing.thumbnailLink
                bookListing.authors = it.authors ?: bookListing.authors
                bookListing.language = it.language ?: bookListing.language
                bookListing.publisher = it.publisher ?: bookListing.publisher
                bookListing.categories = it.categories ?: bookListing.categories
                bookListing.publishedDate = it.publishedDate ?: bookListing.publishedDate
                bookListing.description = it.description ?: bookListing.description
                bookListing.maturityRating = it.maturityRating ?: bookListing.maturityRating
                bookListing.title = it.title
                bookListing.id = it.id ?: bookListing.id
            }
            Log.d("BookRepo", "Adding book listing to Strapi")
            val result = cmsNetworkDatasource.addBookListing(bookListing)
            Log.d("BookRepo", "Book listing added successfully: ${result._id}")
            Result.success(result)
        } catch (e: Exception) {
            Log.e("BookRepo", "Error in addBookListingWithGoogleData", e)
            Result.failure(e)
        }
    }

    private suspend fun testAddBookListingWithGoogleData() {
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
            isbn = "9789026610585", //Narnia
            title = "",
            language = "",
            publisher = "",
            categories = emptyList(),
            publishedDate = "",
            description = "",
            similarBooks = emptyList(),
            createdAt = "",
            updatedAt = "",
            __v = 0,
            published_at = "",
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

    private suspend fun testUpdateBookListingWithIsbn() {
        val isbnToUpdate = "9789026610585" // ISBN of the book to update, has to be one that the logged in user owns

        // Dummy data for update
        val updatedBookListing = BookListing(
            pageCount = 0,
            thumbnailLink = "",
            canBeBorrowed = false,
            canBeSold = false,
            authors = emptyList(),
            keywords = emptyList(),
            extraInfoFromOwner = "",
            maturityRating = "",
            ownerId = "65290e4e3277e881354a4d15",
            isbn = "9789026610585", //Narnia
            title = "",
            language = "",
            publisher = "",
            categories = emptyList(),
            description = "",
            similarBooks = emptyList(),
            createdAt = "",
            updatedAt = "",
            __v = 0,
            published_at = "",
            id = ""
        )

        val updateResult = updateBookListingByIsbn(isbnToUpdate, updatedBookListing)
        if (updateResult.isSuccess) {
            Log.d("BookRepo", "Book listing updated successfully: ${updateResult.getOrNull()?._id}")
        } else {
            Log.e("BookRepo", "Error in updating book listing", updateResult.exceptionOrNull())
        }
    }

    override suspend fun deleteBookListingByIsbnAndOwner(isbn: String): Result<Unit> {
        return try {
            cmsNetworkDatasource.deleteBookListingByIsbnAndOwner(isbn)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("BookListingRepositoryImpl", "Error in deleting book listing with ISBN: $isbn and logged in owner", e)
            Result.failure(e)
        }
    }

    private suspend fun testDeleteBookListingByIsbnAndOwner() {
        val isbnToDelete = "9789026610585"

        val deleteResult = deleteBookListingByIsbnAndOwner(isbnToDelete)
        if (deleteResult.isSuccess) {
            Log.d("BookRepo", "Book listing deleted successfully")
        } else {
            Log.e("BookRepo", "Error in deleting book listing", deleteResult.exceptionOrNull())
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
