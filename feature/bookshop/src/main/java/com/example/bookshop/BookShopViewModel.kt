package com.example.bookshop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.DefaultBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import com.example.model.book.Volume
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class BookShopViewModel @Inject constructor(
    val bookRepository: DefaultBookRepository
) : ViewModel() {
    private val _books = MutableStateFlow<BookShopUiState>(BookShopUiState.Loading)
    val books: StateFlow<BookShopUiState>
        get() = _books

    init {
        loadBooks()
    }

    private fun loadBooks() {
        viewModelScope.launch {
            try {
                val bookList = bookRepository.getAllVolumes()
                _books.value = BookShopUiState.Success(bookList)
            } catch (e: Exception) {
                _books.value = BookShopUiState.Error
            }
        }
    }
}

sealed class BookShopUiState {
    data class Success(val books: List<Volume>) : BookShopUiState()
    object Loading : BookShopUiState()
    object Error : BookShopUiState()

}