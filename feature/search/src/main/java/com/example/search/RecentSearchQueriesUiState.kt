package com.example.search

import com.example.model.RecentSearchQuery

sealed interface RecentSearchQueriesUiState {
    data object Loading : RecentSearchQueriesUiState

    data class Success(
        val recentQueries: List<RecentSearchQuery> = emptyList(),
    ) : RecentSearchQueriesUiState
}