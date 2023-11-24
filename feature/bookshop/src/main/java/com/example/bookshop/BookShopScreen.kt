package com.example.bookshop

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
import com.example.ui.Loading

@Composable
internal fun BookShopRoute(
    onBookClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewmodel: BookShopViewModel = hiltViewModel()
) {
    val bookState by viewmodel.books.collectAsStateWithLifecycle()

    BookShopScreen(
        onBookClick = onBookClick,
        modifier = modifier,
        bookState
    )
}

@Composable
internal fun BookShopScreen(
    onBookClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    state: BookShopUiState
) {
    when (state) {
        is BookShopUiState.Loading -> Loading(modifier)
        is BookShopUiState.Success -> Content(books = state.books, modifier, onBookClick)
        is BookShopUiState.Error -> Text("Error")
    }

}

@Composable
internal fun Content(
    books: List<Volume>,
    modifier: Modifier = Modifier,
    onBookClick: (String) -> Unit
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
