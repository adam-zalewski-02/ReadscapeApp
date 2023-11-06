package com.example.model.book

import kotlinx.serialization.Serializable

@Serializable
data class Volume(
    val id: String,
    val kind: String,
    val etag: String,
    val selfLink: String,
    val volumeInfo: VolumeInfo,
    val saleInfo: SaleInfo,
    val accessInfo: AccessInfo
)