package com.example.network.model.catalog

import kotlinx.serialization.Serializable

@Serializable
data class CatalogRequest (
    val bookId: String
)