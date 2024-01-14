package com.example.network.datasource

import com.example.model.book.BookListing
import com.example.network.CmsNetworkDatasource
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CmsNetworkDatasourceTest {

    private lateinit var cmsNetworkDatasource: CmsNetworkDatasource

    @Before
    fun setup() {
        cmsNetworkDatasource = object : CmsNetworkDatasource {
            override suspend fun getBookListings(start: Int, limit: Int, filters: Map<String, String>): List<BookListing> {
                // Mock implementation, return a dummy list for testing
                return listOf(
                    BookListing(
                        _id = "1",
                        pageCount = 200,
                        thumbnailLink = "thumbnail_link",
                        canBeBorrowed = true,
                        canBeSold = false,
                        authors = listOf("Author1", "Author2"),
                        keywords = listOf("Keyword1", "Keyword2"),
                        extraInfoFromOwner = "Extra info",
                        maturityRating = "MATURE",
                        ownerId = "user123",
                        isbn = "ISBN123",
                        title = "Book Title",
                        language = "English",
                        publisher = "Publisher",
                        categories = listOf("Category1", "Category2"),
                        description = "Book description",
                        similarBooks = emptyList(),
                        published_at = "2022-01-01",
                        createdAt = "2022-01-01",
                        updatedAt = "2022-01-02",
                        __v = 1,
                        id = "1"
                    )
                )
            }

            override suspend fun getSingleBookListing(listingId: String): BookListing? {
                // Mock implementation, return a single BookListing for testing
                return getBookListings(0, 1, emptyMap()).firstOrNull()
            }

            override suspend fun getSingleBookListingByIsbnForCurrentUser(isbn: String): BookListing? {
                // Mock implementation, return a single BookListing for testing
                return getBookListings(0, 1, emptyMap()).firstOrNull()
            }

            override suspend fun addBookListing(bookListing: BookListing): BookListing {
                // Mock implementation, return the same BookListing for testing
                return bookListing.copy(_id = "2")
            }

            override suspend fun updateBookListingByIsbn(isbn: String, updatedBookListing: BookListing): BookListing? {
                // Mock implementation, return the updated BookListing for testing
                return updatedBookListing.copy(updatedAt = "2022-01-03")
            }

            override suspend fun deleteBookListingByIsbnAndOwner(isbn: String): Response<Unit> {
                // Mock implementation, return a successful Response for testing
                return Response.success(Unit)
            }
        }
    }

    @Test
    fun `getBookListings should return a list of BookListings`() = runBlocking {
        // Call the function and assert the result
        val result = cmsNetworkDatasource.getBookListings(0, 10, emptyMap())

        // Mocked implementation returns a single BookListing, so the result should have size 1
        assertEquals(1, result.size)
    }

    @Test
    fun `getSingleBookListing should return a single BookListing`() = runBlocking {
        // Call the function and assert the result
        val result = cmsNetworkDatasource.getSingleBookListing("1")

        // Mocked implementation returns a single BookListing, so the result should not be null
        assertEquals("1", result?.id)
    }

    @Test
    fun `getSingleBookListingByIsbnForCurrentUser should return a single BookListing`() = runBlocking {
        // Call the function and assert the result
        val result = cmsNetworkDatasource.getSingleBookListingByIsbnForCurrentUser("ISBN123")

        // Mocked implementation returns a single BookListing, so the result should not be null
        assertEquals("1", result?.id)
    }

    @Test
    fun `addBookListing should return a new BookListing`() = runBlocking {
        // Create a new BookListing
        val newBookListing = BookListing(
            pageCount = 150,
            thumbnailLink = "new_thumbnail_link",
            canBeBorrowed = true,
            canBeSold = true,
            authors = listOf("New Author"),
            keywords = listOf("New Keyword"),
            extraInfoFromOwner = "New Extra info",
            maturityRating = "TEEN",
            ownerId = "user456",
            isbn = "ISBN456",
            title = "New Book Title",
            language = "Spanish",
            publisher = "New Publisher",
            categories = listOf("New Category"),
            description = "New Book description",
            similarBooks = emptyList(),
            published_at = "2022-01-02",
            createdAt = "2022-01-02",
            updatedAt = "2022-01-02",
            __v = 1,
            id = "2"
        )

        // Call the function and assert the result
        val result = cmsNetworkDatasource.addBookListing(newBookListing)

        // Mocked implementation returns a new BookListing with _id = "2"
        assertEquals("2", result._id)
    }

    @Test
    fun `updateBookListingByIsbn should return an updated BookListing`() = runBlocking {
        // Create an updated BookListing
        val updatedBookListing = BookListing(
            _id = "1",
            pageCount = 180,
            thumbnailLink = "updated_thumbnail_link",
            canBeBorrowed = false,
            canBeSold = true,
            authors = listOf("Updated Author"),
            keywords = listOf("Updated Keyword"),
            extraInfoFromOwner = "Updated Extra info",
            maturityRating = "ADULT",
            ownerId = "user123",
            isbn = "ISBN123",
            title = "Updated Book Title",
            language = "French",
            publisher = "Updated Publisher",
            categories = listOf("Updated Category"),
            description = "Updated Book description",
            similarBooks = emptyList(),
            published_at = "2022-01-01",
            createdAt = "2022-01-01",
            updatedAt = "2022-01-03",
            __v = 1,
            id = "1"
        )

        // Call the function and assert the result
        val result = cmsNetworkDatasource.updateBookListingByIsbn("ISBN123", updatedBookListing)

        // Mocked implementation returns the updated BookListing
        assertEquals("2022-01-03", result?.updatedAt)
    }
}