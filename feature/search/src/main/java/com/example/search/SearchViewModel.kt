package com.example.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.DefaultBookRepository
import com.example.data.repository.RecentSearchRepository
import com.example.domain.GetRecentSearchQueriesUseCase
import com.example.model.book.Volume
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val bookRepository: DefaultBookRepository,
    private val savedStateHandle: SavedStateHandle,
    private val recentSearchRepository: RecentSearchRepository,
    getRecentSearchQueriesUseCase: GetRecentSearchQueriesUseCase
): ViewModel(){
    private val _books = MutableStateFlow<SearchResultUiState>(SearchResultUiState.Loading)
    val books: StateFlow<SearchResultUiState>
        get() = _books

    val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = "")
    fun searchBooks(query: String) {
        viewModelScope.launch {
            try {
                val bookList = bookRepository.getVolumesByTitle(query)
                _books.value = SearchResultUiState.Success(bookList)
            } catch (e: Exception) {
                _books.value = SearchResultUiState.Error
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        savedStateHandle[SEARCH_QUERY] = query
    }
}
private const val SEARCH_QUERY = "searchQuery"
sealed class SearchResultUiState {
    data object  Loading : SearchResultUiState()
    data object EmptyQuery : SearchResultUiState()
    data object  LoadFailed : SearchResultUiState()
    data class Success(val volumes: List<Volume> = emptyList()) : SearchResultUiState() {
        fun isEmpty(): Boolean = volumes.isEmpty()
    }
    data object SearchNotReady : SearchResultUiState()
    data object Error : SearchResultUiState()
}