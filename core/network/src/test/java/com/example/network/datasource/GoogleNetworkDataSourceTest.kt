package com.example.network.datasource

import com.example.model.book.AccessInfo
import com.example.model.book.EpubAvailability
import com.example.model.book.PdfAvailability
import com.example.model.book.SaleInfo
import com.example.model.book.Volume
import com.example.model.book.VolumeInfo
import com.example.network.GoogleNetworkDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GoogleNetworkDataSourceTest {

    private lateinit var googleNetworkDataSource: GoogleNetworkDataSource

    @Before
    fun setup() {
        googleNetworkDataSource = object : GoogleNetworkDataSource {
            override suspend fun getAllVollumes(): List<Volume> {
                // Mock implementation, return a dummy list for testing
                return listOf(
                    createDummyVolume("1", "Title1", listOf("Author1")),
                    createDummyVolume("2", "Title2", listOf("Author2"))
                )
            }

            override suspend fun getVolumeById(volumeId: String): Volume {
                // Mock implementation, return a dummy Volume for testing
                return createDummyVolume(volumeId, "Title$volumeId", listOf("Author$volumeId"))
            }

            override suspend fun getVolumesByTitle(title: String): List<Volume> {
                // Mock implementation, return a dummy list for testing
                return listOf(
                    createDummyVolume("1", title, listOf("Author1")),
                    createDummyVolume("2", title, listOf("Author2"))
                )
            }

            override suspend fun getVolumesByIsbn(isbn: String): List<Volume> {
                // Mock implementation, return a dummy list for testing
                return listOf(createDummyVolume(isbn, "Title$isbn", listOf("Author$isbn")))
            }

            private fun createDummyVolume(id: String, title: String, authors: List<String>): Volume {
                return Volume(
                    id = id,
                    kind = "books#volume",
                    etag = "dummyEtag",
                    selfLink = "dummySelfLink",
                    volumeInfo = VolumeInfo(
                        title = title,
                        subtitle = null,
                        authors = authors,
                        publishedDate = null,
                        description = null,
                        industryIdentifiers = null,
                        readingModes = null,
                        pageCount = null,
                        printType = null,
                        maturityRating = null,
                        allowAnonLogging = null,
                        contentVersion = null,
                        panelizationSummary = null,
                        imageLinks = null,
                        language = null,
                        previewLink = null,
                        infoLink = null,
                        canonicalVolumeLink = null,
                        publisher = null,
                        categories = null,
                        id = null
                    ),
                    saleInfo = SaleInfo(
                        country = "US",
                        saleability = "FOR_SALE",
                        isEbook = false
                    ),
                    accessInfo = AccessInfo(
                        country = "US",
                        viewability = "PARTIAL",
                        embeddable = true,
                        publicDomain = false,
                        textToSpeechPermission = "ALLOWED",
                        epub = EpubAvailability(isAvailable = false),
                        pdf = PdfAvailability(isAvailable = true),
                        webReaderLink = "dummyWebReaderLink",
                        accessViewStatus = "SAMPLE",
                        quoteSharingAllowed = true
                    )
                )
            }
        }
    }

    @Test
    fun `getAllVollumes should return a list of Volume`() = runBlocking {
        // Call the function and assert the result
        fun createDummyVolume(id: String, title: String, authors: List<String>): Volume {
            return Volume(
                id = id,
                kind = "books#volume",
                etag = "dummyEtag",
                selfLink = "dummySelfLink",
                volumeInfo = VolumeInfo(
                    title = title,
                    subtitle = null,
                    authors = authors,
                    publishedDate = null,
                    description = null,
                    industryIdentifiers = null,
                    readingModes = null,
                    pageCount = null,
                    printType = null,
                    maturityRating = null,
                    allowAnonLogging = null,
                    contentVersion = null,
                    panelizationSummary = null,
                    imageLinks = null,
                    language = null,
                    previewLink = null,
                    infoLink = null,
                    canonicalVolumeLink = null,
                    publisher = null,
                    categories = null,
                    id = null
                ),
                saleInfo = SaleInfo(
                    country = "US",
                    saleability = "FOR_SALE",
                    isEbook = false
                ),
                accessInfo = AccessInfo(
                    country = "US",
                    viewability = "PARTIAL",
                    embeddable = true,
                    publicDomain = false,
                    textToSpeechPermission = "ALLOWED",
                    epub = EpubAvailability(isAvailable = false),
                    pdf = PdfAvailability(isAvailable = true),
                    webReaderLink = "dummyWebReaderLink",
                    accessViewStatus = "SAMPLE",
                    quoteSharingAllowed = true
                )
            )
        }
        val result = googleNetworkDataSource.getAllVollumes()
        val expected = listOf(
            createDummyVolume("1", "Title1", listOf("Author1")),
            createDummyVolume("2", "Title2", listOf("Author2"))
        )

        assertEquals(expected, result)
    }

    @Test
    fun `getVolumeById should return a specific Volume`() = runBlocking {
        // Call the function and assert the result
        fun createDummyVolume(id: String, title: String, authors: List<String>): Volume {
            return Volume(
                id = id,
                kind = "books#volume",
                etag = "dummyEtag",
                selfLink = "dummySelfLink",
                volumeInfo = VolumeInfo(
                    title = title,
                    subtitle = null,
                    authors = authors,
                    publishedDate = null,
                    description = null,
                    industryIdentifiers = null,
                    readingModes = null,
                    pageCount = null,
                    printType = null,
                    maturityRating = null,
                    allowAnonLogging = null,
                    contentVersion = null,
                    panelizationSummary = null,
                    imageLinks = null,
                    language = null,
                    previewLink = null,
                    infoLink = null,
                    canonicalVolumeLink = null,
                    publisher = null,
                    categories = null,
                    id = null
                ),
                saleInfo = SaleInfo(
                    country = "US",
                    saleability = "FOR_SALE",
                    isEbook = false
                ),
                accessInfo = AccessInfo(
                    country = "US",
                    viewability = "PARTIAL",
                    embeddable = true,
                    publicDomain = false,
                    textToSpeechPermission = "ALLOWED",
                    epub = EpubAvailability(isAvailable = false),
                    pdf = PdfAvailability(isAvailable = true),
                    webReaderLink = "dummyWebReaderLink",
                    accessViewStatus = "SAMPLE",
                    quoteSharingAllowed = true
                )
            )
        }
        val result = googleNetworkDataSource.getVolumeById("1")
        val expected = createDummyVolume("1", "Title1", listOf("Author1"))

        assertEquals(expected, result)
    }

    @Test
    fun `getVolumesByTitle should return a list of Volume with the given title`() = runBlocking {
        // Call the function and assert the result
        fun createDummyVolume(id: String, title: String, authors: List<String>): Volume {
            return Volume(
                id = id,
                kind = "books#volume",
                etag = "dummyEtag",
                selfLink = "dummySelfLink",
                volumeInfo = VolumeInfo(
                    title = title,
                    subtitle = null,
                    authors = authors,
                    publishedDate = null,
                    description = null,
                    industryIdentifiers = null,
                    readingModes = null,
                    pageCount = null,
                    printType = null,
                    maturityRating = null,
                    allowAnonLogging = null,
                    contentVersion = null,
                    panelizationSummary = null,
                    imageLinks = null,
                    language = null,
                    previewLink = null,
                    infoLink = null,
                    canonicalVolumeLink = null,
                    publisher = null,
                    categories = null,
                    id = null
                ),
                saleInfo = SaleInfo(
                    country = "US",
                    saleability = "FOR_SALE",
                    isEbook = false
                ),
                accessInfo = AccessInfo(
                    country = "US",
                    viewability = "PARTIAL",
                    embeddable = true,
                    publicDomain = false,
                    textToSpeechPermission = "ALLOWED",
                    epub = EpubAvailability(isAvailable = false),
                    pdf = PdfAvailability(isAvailable = true),
                    webReaderLink = "dummyWebReaderLink",
                    accessViewStatus = "SAMPLE",
                    quoteSharingAllowed = true
                )
            )
        }
        val result = googleNetworkDataSource.getVolumesByTitle("Sample Title")
        val expected = listOf(
            createDummyVolume("1", "Sample Title", listOf("Author1")),
            createDummyVolume("2", "Sample Title", listOf("Author2"))
        )

        assertEquals(expected, result)
    }

    @Test
    fun `getVolumesByIsbn should return a list of Volume with the given ISBN`() = runBlocking {
        // Call the function and assert the result
        fun createDummyVolume(id: String, title: String, authors: List<String>): Volume {
            return Volume(
                id = id,
                kind = "books#volume",
                etag = "dummyEtag",
                selfLink = "dummySelfLink",
                volumeInfo = VolumeInfo(
                    title = title,
                    subtitle = null,
                    authors = authors,
                    publishedDate = null,
                    description = null,
                    industryIdentifiers = null,
                    readingModes = null,
                    pageCount = null,
                    printType = null,
                    maturityRating = null,
                    allowAnonLogging = null,
                    contentVersion = null,
                    panelizationSummary = null,
                    imageLinks = null,
                    language = null,
                    previewLink = null,
                    infoLink = null,
                    canonicalVolumeLink = null,
                    publisher = null,
                    categories = null,
                    id = null
                ),
                saleInfo = SaleInfo(
                    country = "US",
                    saleability = "FOR_SALE",
                    isEbook = false
                ),
                accessInfo = AccessInfo(
                    country = "US",
                    viewability = "PARTIAL",
                    embeddable = true,
                    publicDomain = false,
                    textToSpeechPermission = "ALLOWED",
                    epub = EpubAvailability(isAvailable = false),
                    pdf = PdfAvailability(isAvailable = true),
                    webReaderLink = "dummyWebReaderLink",
                    accessViewStatus = "SAMPLE",
                    quoteSharingAllowed = true
                )
            )
        }
        val result = googleNetworkDataSource.getVolumesByIsbn("1234567890")
        val expected = listOf(createDummyVolume("1234567890", "Title1234567890", listOf("Author1234567890")))

        assertEquals(expected, result)
    }
}