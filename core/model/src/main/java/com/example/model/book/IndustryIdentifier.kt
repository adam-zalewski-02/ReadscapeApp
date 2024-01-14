package com.example.model.book

import kotlinx.serialization.Serializable

@Serializable
data class IndustryIdentifier(
    val type: String,
    val identifier: String
)