package com.example.network

import com.example.model.book.Volume

interface GoogleNetworkDataSource {
    suspend fun getAllVollumes(): List<Volume>
    suspend fun getVolumeById(volumeId: String): Volume
    suspend fun getVolumesByTitle(title: String) : List<Volume>
}