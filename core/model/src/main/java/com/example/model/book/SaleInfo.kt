package com.example.model.book

import kotlinx.serialization.Serializable

@Serializable
data class SaleInfo(
    val country: String,
    val saleability: String,
    val isEbook: Boolean
)