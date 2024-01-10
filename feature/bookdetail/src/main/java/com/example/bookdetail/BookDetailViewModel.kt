package com.example.bookdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.BookListingRepository
import com.example.data.repository.DefaultBookRepository
import com.example.data.repository.DefaultUserRepository
import com.example.model.CurrentUserManager
import com.example.model.book.BookListing
import com.example.model.book.Volume
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val bookRepository: DefaultBookRepository,
    private val userRepository: DefaultUserRepository,
    private val bookListingRepository: BookListingRepository,
): ViewModel() {
    private val _bookDetails = MutableStateFlow<BookDetailUiState>(BookDetailUiState.Loading)
    val bookDetails: StateFlow<BookDetailUiState> = _bookDetails

    private val _bookListingDetails = MutableStateFlow<BookListing?>(null)
    val bookListingDetails: StateFlow<BookListing?> = _bookListingDetails

    private val _bookListingExists = MutableStateFlow<Boolean>(false)
    val bookListingExists: StateFlow<Boolean> = _bookListingExists

    private val previousStates = mutableListOf<BookDetailUiState>()

    fun onBackPressed() {
        if (previousStates.isNotEmpty()) {
            _bookDetails.value = previousStates.removeLast()
        }
    }

    fun checkIfBookListingExists(isbn: String) {
        viewModelScope.launch {
            val exists = bookListingRepository.getSingleBookListingByIsbnForCurrentUser(isbn) != null
            _bookListingExists.value = exists
        }
    }

    fun getBookDetails(bookId: String) {
        viewModelScope.launch {
            try {
                val book = bookRepository.getVolumeById(bookId)
                _bookDetails.value = BookDetailUiState.Success(book)
                previousStates.add(_bookDetails.value)
            } catch (e: Exception) {
                //_bookDetails.value = BookDetailUiState.Error(e.localizedMessage ?: "Unkown error")
            }
        }
    }

    fun updateBookListing(updatedBookListing: BookListing) {
        viewModelScope.launch {
            val result = bookListingRepository.updateBookListingByIsbn(updatedBookListing.isbn, updatedBookListing)
            // Handle the result of the update operation
            result.onSuccess {
                // Update UI state or show confirmation message
            }
            result.onFailure {
                // Handle error
            }
        }
    }

    fun getBookListingDetails(isbn: String) {
        viewModelScope.launch {
            _bookListingDetails.value = bookListingRepository.getSingleBookListingByIsbnForCurrentUser(isbn)
        }
    }

    fun publishBookListing(isbn: String) {
        viewModelScope.launch {
            val bookListing = bookListingRepository.getSingleBookListingByIsbnForCurrentUser(isbn)
            if (bookListing != null) {
                _bookDetails.value = BookDetailUiState.EditBookListing(bookListing)
            } else {
                val newBookListing = BookListing(
                    pageCount = 0,
                    thumbnailLink = "",
                    canBeBorrowed = true,
                    canBeSold = true,
                    authors = listOf(),
                    keywords = listOf(),
                    extraInfoFromOwner = "",
                    maturityRating = "",
                    ownerId = CurrentUserManager.getCurrentUser()?.userId.toString(),
                    isbn = isbn,
                    title = "",
                    language = "",
                    publisher = "",
                    categories = listOf(),
                    publishedDate = null,
                    description = "",
                    similarBooks = listOf(),
                    createdAt = "",
                    updatedAt = "",
                    __v = 0,
                    published_at = "",
                    id = ""
                )
                val result = bookListingRepository.addBookListingWithGoogleData(newBookListing)
                result.fold(
                    onSuccess = {
                        // Handle success, such as updating UI state to show the new listing
                        _bookDetails.value = BookDetailUiState.ViewBookListing(it)
                    },
                    onFailure = {
                        // Handle failure, such as showing an error message
                        _bookDetails.value = BookDetailUiState.Error
                    }
                )
            }
        }
    }



    // Function to switch to edit book listing screen
    fun editBookListing(isbn: String) {
        viewModelScope.launch {
            val bookListing = bookListingRepository.getSingleBookListingByIsbnForCurrentUser(isbn)
            bookListing?.let {
                _bookDetails.value = BookDetailUiState.EditBookListing(it)
            }
        }
    }

    // Function to switch to view book listing screen
    fun viewBookListing(isbn: String) {
        viewModelScope.launch {
            val bookListing = bookListingRepository.getSingleBookListingByIsbnForCurrentUser(isbn)
            bookListing?.let {
                _bookDetails.value = BookDetailUiState.ViewBookListing(it)
            }
        }
    }

    fun addBookToFavorites(volumeId: String) {
        viewModelScope.launch {
            try {
                val currentUser = CurrentUserManager.getCurrentUser()
                val response = userRepository.insertIntoCollection(currentUser?.userId.toString(), volumeId)
                println(response)
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }


}


sealed interface BookDetailUiState {
    data class Success(val volume: Volume) : BookDetailUiState
    object Error : BookDetailUiState
    object Loading : BookDetailUiState
    data class EditBookListing(val bookListing: BookListing) : BookDetailUiState
    data class ViewBookListing(val bookListing: BookListing) : BookDetailUiState

}

