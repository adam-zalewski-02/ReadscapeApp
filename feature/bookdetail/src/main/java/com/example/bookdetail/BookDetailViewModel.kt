package com.example.bookdetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.BookListingRepository
import com.example.data.repository.DefaultBookRepository
import com.example.data.repository.DefaultUserRepository
import com.example.model.CurrentUserManager
import com.example.model.Transaction
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

    private val _bookListingExists = MutableStateFlow(false)
    val bookListingExists: StateFlow<Boolean> = _bookListingExists

    private val _volume = MutableStateFlow<Volume?>(null)
    private val volume: MutableStateFlow<Volume?> = _volume

    fun returnToBookDetailScreen() {
        _bookDetails.value = BookDetailUiState.Success(volume.value!!)
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
                _volume.value = book
                _bookDetails.value = BookDetailUiState.Success(book)
            } catch (_: Exception) {
            }
        }
    }

    fun updateBookListing(updatedBookListing: BookListing) {
        viewModelScope.launch {
            Log.d("EditBookScreen", "Book listing to be passed in the viewmodel: $updatedBookListing")
            val result = bookListingRepository.updateBookListingByIsbn(updatedBookListing.isbn, updatedBookListing)
            result.onSuccess {
                returnToBookDetailScreen()
            }
            result.onFailure {
            }
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
                        _bookDetails.value = BookDetailUiState.ViewBookListing(it)
                    },
                    onFailure = {
                        _bookDetails.value = BookDetailUiState.Error
                    }
                )
            }
        }
    }

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

    fun lendOutBook(lenderId: String, volumeId: String, isbn: String) {
        viewModelScope.launch {
            try {
                val currentUser = CurrentUserManager.getCurrentUser()
                val response = userRepository.insertIntoCollection(lenderId, volumeId)
                println(response)
                if (currentUser != null) {
                    val transaction = userRepository.insertIntoTransactions(currentUser.userId, lenderId, isbn, 30)
                    println(transaction)
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    fun deleteBookListing(isbn: String) {
        viewModelScope.launch {
            val result = bookListingRepository.deleteBookListingByIsbnAndOwner(isbn)
            result.onSuccess {
                returnToBookDetailScreen()
            }
            result.onFailure {
            }
        }
    }
}

sealed interface BookDetailUiState {
    data class Success(val volume: Volume) : BookDetailUiState
    data object Error : BookDetailUiState
    data object Loading : BookDetailUiState
    data class EditBookListing(val bookListing: BookListing) : BookDetailUiState
    data class ViewBookListing(val bookListing: BookListing) : BookDetailUiState
}

