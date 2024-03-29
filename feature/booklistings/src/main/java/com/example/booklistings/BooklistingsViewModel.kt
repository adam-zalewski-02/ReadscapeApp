package com.example.booklistings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.BookListingRepository
import com.example.model.book.BookListing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookListingsViewModel @Inject constructor(
    private val bookListingRepository: BookListingRepository
) : ViewModel() {

    private val _bookListingsState = MutableStateFlow<BookListingsState>(BookListingsState.Loading)
    val bookListingsState: StateFlow<BookListingsState> = _bookListingsState.asStateFlow()

    private val _selectedBookListing = MutableStateFlow<BookListing?>(null)
    val selectedBookListing: StateFlow<BookListing?> = _selectedBookListing

    private val _ownerEmail = MutableStateFlow<String>("")
    val ownerEmail: StateFlow<String> = _ownerEmail.asStateFlow()

    init {
        loadBookListings()
    }

    fun selectBookListing(bookListing: BookListing) {
        _selectedBookListing.value = bookListing
    }

    fun clearSelectedBookListing() {
        _selectedBookListing.value = null
    }

    fun getBookListingsByIds(ids: List<String>): Flow<List<BookListing>> = flow {
        coroutineScope {
            val bookListings = ids.map { id ->
                async {
                    try {
                        bookListingRepository.getSingleBookListing(id)
                    } catch (e: Exception) {
                        null
                    }
                }
            }.awaitAll().filterNotNull()
            emit(bookListings)
        }
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

    fun fetchOwnerEmail(ownerId: String) {
        viewModelScope.launch {
            val emailResult = bookListingRepository.getOwnerEmailById(ownerId)
            _ownerEmail.value = when {
                emailResult.isSuccess -> emailResult.getOrNull() ?: "No email found"
                else -> "Error fetching email: ${emailResult.exceptionOrNull()?.message}"
            }
        }
    }
}

sealed class BookListingsState {
    data object Loading : BookListingsState()
    data class Success(val bookListings: List<BookListing>) : BookListingsState()
    data class Error(val message: String) : BookListingsState()
}
