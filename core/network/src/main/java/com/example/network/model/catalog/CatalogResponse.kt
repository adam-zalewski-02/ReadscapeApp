package com.example.network.model.catalog

import kotlinx.serialization.Serializable

@Serializable
data class CatalogResponse(
    val _id: String,
    val user_id: String,
    val data: List<String>
)