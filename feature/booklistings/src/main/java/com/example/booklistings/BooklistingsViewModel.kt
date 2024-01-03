package com.example.booklistings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.BookListingRepository
import com.example.model.book.BookListing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookListingsViewModel @Inject constructor(
    private val bookListingRepository: BookListingRepository
) : ViewModel() {

    private val _bookListingsState = MutableStateFlow<BookListingsState>(BookListingsState.Loading)
    val bookListingsState: StateFlow<BookListingsState> = _bookListingsState.asStateFlow()

    init {
        loadBookListings()
    }

    private fun loadBookListings() {
        viewModelScope.launch {
            try {
                val listings = bookListingRepository.getFilteredBookListings(0, 30, mapOf()).first()
                _bookListingsState.value = BookListingsState.Success(listings)
            } catch (e: Exception) {
                _bookListingsState.value = BookListingsState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun applyFilter(filters: Map<String, String>) {
        viewModelScope.launch {
            try {
                val listings = bookListingRepository.getFilteredBookListings(0, 30, filters).first()
                _bookListingsState.value = BookListingsState.Success(listings)
            } catch (e: Exception) {
                _bookListingsState.value = BookListingsState.Error(e.message ?: "Unknown error")
            }
        }
    }

}

sealed class BookListingsState {
    object Loading : BookListingsState()
    data class Success(val bookListings: List<BookListing>) : BookListingsState()
    data class Error(val message: String) : BookListingsState()
}
