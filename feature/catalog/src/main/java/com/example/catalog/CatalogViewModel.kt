package com.example.catalog

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
class CatalogViewModel @Inject constructor(
    private val userRepository: DefaultUserRepository,
    private val bookRepository: DefaultBookRepository
): ViewModel() {
    private val _books = MutableStateFlow<CatalogUiState>(CatalogUiState.Loading)
    val books: StateFlow<CatalogUiState>
        get() = _books

    init {
        loadBooks()
    }

    private fun loadBooks() {
        viewModelScope.launch {
            try {
                val catalogResponse = CurrentUserManager.getCurrentUser()
                    ?.let { userRepository.getCollection(it.userId) }
                if (catalogResponse != null) {
                    _books.value = CatalogUiState.SuccessWithVolumes(catalogResponse.data)
                    getBooks(catalogResponse.data)
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun getBooks(bookIds: List<String>) {
        viewModelScope.launch {
            try {
                val volumeList =  mutableListOf<Volume>()
                bookIds.forEach { volumeId ->
                    val volume = bookRepository.getVolumeById(volumeId)
                    volumeList.add(volume)
                    println(volumeId)
                }
                _books.value = CatalogUiState.Success(volumeList)
            } catch (e: Exception) {

            }
        }
    }

}

sealed class CatalogUiState {
    data class SuccessWithVolumes(val books: List<String>) : CatalogUiState()
    data class Success(val volumes: List<Volume>) : CatalogUiState()
    object Loading: CatalogUiState()
    object Error: CatalogUiState()
}