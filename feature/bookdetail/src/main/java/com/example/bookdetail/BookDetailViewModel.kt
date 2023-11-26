package com.example.bookdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.DefaultBookRepository
import com.example.data.repository.DefaultUserRepository
import com.example.model.CurrentUserManager
import com.example.model.book.Volume
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val bookRepository: DefaultBookRepository,
    private val userRepository: DefaultUserRepository
): ViewModel() {
    private val _bookDetails = MutableStateFlow<BookDetailUiState>(BookDetailUiState.Loading)
    val bookDetails: StateFlow<BookDetailUiState> = _bookDetails

    fun getBookDetails(bookId: String) {
        viewModelScope.launch {
            try {
                val book = bookRepository.getVolumeById(bookId)
                _bookDetails.value = BookDetailUiState.Success(book)
            } catch (e: Exception) {
                //_bookDetails.value = BookDetailUiState.Error(e.localizedMessage ?: "Unkown error")
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
    object  Error : BookDetailUiState
    object Loading : BookDetailUiState
}