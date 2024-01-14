package com.example.catalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.book.Volume
import com.example.ui.BookItem
import com.example.designsystem.component.Loading
import com.example.model.CurrentUserManager
import com.example.model.NfcHandler

@Composable
internal fun CatalogRoute(
    onBookClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewmodel: CatalogViewModel = hiltViewModel()
) {
    val catalogState by viewmodel.books.collectAsStateWithLifecycle()
    val nfcHandler = NfcHandler(LocalContext.current)
    CatalogScreen(
        onBookClick = onBookClick,
        modifier = modifier,
        catalogState,
        nfcHandler = nfcHandler
    )
}

@Composable
internal fun CatalogScreen(
    onBookClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    state: CatalogUiState,
    nfcHandler: NfcHandler
) {
    when(state) {
        is CatalogUiState.Loading -> Loading(modifier)
        is CatalogUiState.Success -> Content(onBookClick,books = state.books, modifier, nfcHandler)
        is CatalogUiState.Error -> Text("No books added")
    }
}

@Composable
internal fun Content(
    onBookClick: (String) -> Unit,
    books: List<Volume>,
    modifier: Modifier = Modifier,
    nfcHandler: NfcHandler
) {
        // Your existing LazyColumn for books
        LazyColumn {

            items(books) { book ->
                BookItem(
                    book = book,
                    onBookClick = { onBookClick(book.id) }
                )
            }
        }
    Button(
        onClick = {
            CurrentUserManager.getCurrentUser()?.let { nfcHandler.setHceData(it.userId) }
        }
    ) {
        Text(text = "Lend a book")
    }
    }



