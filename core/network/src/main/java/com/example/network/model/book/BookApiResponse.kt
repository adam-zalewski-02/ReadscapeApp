package com.example.network.model.book

import com.example.model.book.Volume
import kotlinx.serialization.Serializable

@Serializable
data class BookApiResponse(
    val kind: String,
    val totalItems: Int,
    val items: List<Volume>
)
