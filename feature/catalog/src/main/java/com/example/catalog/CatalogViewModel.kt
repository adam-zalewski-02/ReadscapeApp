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
import android.nfc.NfcAdapter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.content.Context
import java.nio.charset.Charset

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val userRepository: DefaultUserRepository,
    private val bookRepository: DefaultBookRepository
) : ViewModel() {
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
                    val bookIds = catalogResponse.data
                    val books = getItemsForIds(bookIds)
                    _books.value = CatalogUiState.Success(books)
                    println(_books.value)
                }
            } catch (e: Exception) {
                println("---------------------------------------------")
                println(e.message.toString())
                _books.value = CatalogUiState.Error
            }
        }
    }

    private suspend fun getItemsForIds(ids: List<String>): List<Volume> {
        val itemsList = mutableListOf<Volume>()
        try {
            for (id in ids) {
                val item = bookRepository.getVolumeById(id)
                itemsList.add(item)
            }
        } catch (e: Exception) {
            _books.value = CatalogUiState.Error
            println(e.message.toString())
        }
        return itemsList
    }
}



sealed class CatalogUiState {
    data class Success(val books: List<Volume>) : CatalogUiState()
    object Loading : CatalogUiState()
    object Error : CatalogUiState()
}
