package com.example.catalog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.book.Volume
import com.example.ui.BookItem
import com.example.designsystem.component.Loading

@Composable
internal fun CatalogRoute(
    onBookClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewmodel: CatalogViewModel = hiltViewModel()
) {
    val catalogState by viewmodel.books.collectAsStateWithLifecycle()
    CatalogScreen(
        onBookClick = onBookClick,
        modifier = modifier,
        catalogState
    )
}

@Composable
internal fun CatalogScreen(
    onBookClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    state: CatalogUiState
) {
    when(state) {
        is CatalogUiState.Loading -> Loading(modifier)
        is CatalogUiState.Success -> Content(onBookClick,books = state.books, modifier)
        is CatalogUiState.Error -> Text("Error")
    }
}

@Composable
internal fun Content(
    onBookClick: (String) -> Unit,
    books: List<Volume>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(books) { book ->
            BookItem(
                book = book,
                onBookClick = {onBookClick(book.id)}
            )
        }
    }
}