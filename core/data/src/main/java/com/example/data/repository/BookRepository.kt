package com.example.data.repository
import com.example.model.book.Volume
import com.example.network.GoogleNetworkDataSource
import javax.inject.Inject

interface BookRepository {
    suspend fun getAllVolumes(): List<Volume>
    suspend fun getVolumeById(volumeId: String): Volume

    suspend fun getVolumesByTitle(title: String): List<Volume>
}

class DefaultBookRepository @Inject constructor(
    private val dataSource: GoogleNetworkDataSource
) : BookRepository {
    override suspend fun getAllVolumes(): List<Volume> {
        return dataSource.getAllVollumes()
    }

    override suspend fun getVolumeById(volumeId: String): Volume {
        return dataSource.getVolumeById(volumeId)
    }

    override suspend fun getVolumesByTitle(title: String): List<Volume> {
        return dataSource.getVolumesByTitle(title)
    }

}