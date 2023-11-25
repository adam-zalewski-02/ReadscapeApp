package com.example.network.model.catalog

import kotlinx.serialization.Serializable

@Serializable
data class CatalogPostResponse (
    val success: Boolean,
    val message: String
)