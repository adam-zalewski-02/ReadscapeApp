package com.example.bookshop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun BookShopRoute(
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewmodel: BookShopViewModel = hiltViewModel()
) {
    val bookState by viewmodel.books.collectAsState()

    BookShopScreen(
        onBookClick = onBookClick,
        modifier = modifier
    )
}

@Composable
internal fun BookShopScreen(
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier
) {

}