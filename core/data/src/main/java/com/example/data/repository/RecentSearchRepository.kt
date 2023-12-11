package com.example.data.repository

import com.example.database.dao.RecentSearchQueryDao
import com.example.database.model.RecentSearchQueryEntity
import com.example.data.model.RecentSearchQuery
import com.example.data.model.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import javax.inject.Inject

interface RecentSearchRepository {
    fun getRecentSearchQueries(limit: Int): Flow<List<RecentSearchQuery>>

    suspend fun insertOrReplaceRecentSearch(searchQuery: String)

    suspend fun  clearRecentSearches()
}

class DefaultRecentSearchRepository @Inject constructor(
    private val recentSearchQueryDao: RecentSearchQueryDao
) : RecentSearchRepository {
    override suspend fun insertOrReplaceRecentSearch(searchQuery: String) {
        recentSearchQueryDao.insertOrReplaceRecentSearchQuery(
            RecentSearchQueryEntity(
                query = searchQuery,
                queriedDate = Clock.System.now(),
            ),
        )
    }

    override fun getRecentSearchQueries(limit: Int): Flow<List<RecentSearchQuery>> =
        recentSearchQueryDao.getRecentSearchQueryEntities(limit).map { searchQueries ->
            searchQueries.map {
                it.asExternalModel()
            }
        }

    override suspend fun clearRecentSearches() = recentSearchQueryDao.clearRecentSearchQueries()
}