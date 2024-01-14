package com.example.model.book
import kotlinx.serialization.Serializable

@Serializable
data class AccessInfo(
    val country: String,
    val viewability: String,
    val embeddable: Boolean,
    val publicDomain: Boolean,
    val textToSpeechPermission: String,
    val epub: EpubAvailability,
    val pdf: PdfAvailability,
    val webReaderLink: String,
    val accessViewStatus: String,
    val quoteSharingAllowed: Boolean
)

@Serializable
data class EpubAvailability(
    val isAvailable: Boolean
)

@Serializable
data class PdfAvailability(
    val isAvailable: Boolean
)

