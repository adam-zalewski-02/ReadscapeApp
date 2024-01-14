package com.example.data.repository
import com.example.model.book.Volume
import com.example.network.GoogleNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import com.example.common.network.Dispatcher
import com.example.common.network.ReadscapeDispatchers.IO

interface BookRepository {
    fun getAllVolumes(): Flow<List<Volume>>
    suspend fun getVolumeById(volumeId: String): Volume

    suspend fun getVolumesByTitle(title: String): List<Volume>
}

class DefaultBookRepository @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val dataSource: GoogleNetworkDataSource,
) : BookRepository {
    override fun getAllVolumes(): Flow<List<Volume>> = flow {
        emit(
            dataSource.getAllVollumes().map {
                Volume(
                    id = it.id,
                    kind = it.kind,
                    etag = it.etag,
                    selfLink = it.selfLink,
                    volumeInfo = it.volumeInfo,
                    saleInfo = it.saleInfo,
                    accessInfo = it.accessInfo
                )
            },
        )
    }.flowOn(ioDispatcher)

    override suspend fun getVolumeById(volumeId: String): Volume {
        return dataSource.getVolumeById(volumeId)
    }

    override suspend fun getVolumesByTitle(title: String): List<Volume> {
        return dataSource.getVolumesByTitle(title)
    }

}