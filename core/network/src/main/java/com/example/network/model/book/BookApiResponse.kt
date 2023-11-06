package com.example.network.model.book

import kotlinx.serialization.Serializable
import com.example.model.book.Volume
@Serializable
data class BookApiResponse(
    val kind: String,
    val totalItems: Int,
    val items: List<Volume>
)
