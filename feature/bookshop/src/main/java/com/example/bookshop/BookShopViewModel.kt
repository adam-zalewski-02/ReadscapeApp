package com.example.bookshop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.DefaultBookRepository
import com.example.model.book.Volume
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BookShopViewModel @Inject constructor(
    bookRepository: DefaultBookRepository
) : ViewModel() {
    val booksUiState: StateFlow<BookShopUiState> =
        bookRepository.getAllVolumes()
            .map<List<Volume>, BookShopUiState>(BookShopUiState::Success)
            .onStart {emit(BookShopUiState.Loading)}
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = BookShopUiState.Loading
            )
}



sealed interface BookShopUiState {
    data class Success(val books: List<Volume>) : BookShopUiState
    data object Loading : BookShopUiState
    data object Error : BookShopUiState

}