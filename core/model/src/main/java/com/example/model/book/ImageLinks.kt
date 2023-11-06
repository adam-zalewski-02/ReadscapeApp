package com.example.model.book

import kotlinx.serialization.Serializable

@Serializable
data class ImageLinks(
    val smallThumbnail: String,
    val thumbnail: String
)
