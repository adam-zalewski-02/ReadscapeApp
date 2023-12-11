package com.example.domain
import com.example.data.repository.RecentSearchRepository
import com.example.data.model.RecentSearchQuery
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentSearchQueriesUseCase @Inject constructor(
    private val recentSearchRepository: RecentSearchRepository,
) {
    operator fun invoke(limit: Int = 10) : Flow<List<RecentSearchQuery>> =
        recentSearchRepository.getRecentSearchQueries(limit)
}