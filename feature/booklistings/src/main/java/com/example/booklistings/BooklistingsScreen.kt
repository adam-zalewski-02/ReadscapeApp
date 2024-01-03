package com.example.booklistings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.model.book.BookListing

@Composable
fun BookListingsScreen(
    viewModel: BookListingsViewModel = hiltViewModel()
) {
    val state by viewModel.bookListingsState.collectAsState()

    when (state) {
        is BookListingsState.Loading -> {
            // Display loading UI
        }
        is BookListingsState.Success -> {
            val bookListings = (state as BookListingsState.Success).bookListings
            BookListingsList(bookListings)
        }
        is BookListingsState.Error -> {
            // Display error UI
        }
    }
}

@Composable
fun BookListingsList(bookListings: List<BookListing>) {
    LazyColumn {
        items(bookListings) { bookListing ->
            BookListItem(bookListing)
        }
    }
}

@Composable
fun BookListItem(bookListing: BookListing) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(bookListing.thumbnailLink),
            contentDescription = "Book Thumbnail",
            modifier = Modifier.size(60.dp, 90.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(16.dp))
        Column {
            Text(text = bookListing.title, style = MaterialTheme.typography.titleMedium)
            Text(text = "Author: ${bookListing.authors.joinToString()}", style = MaterialTheme.typography.headlineLarge)
        }
    }
}
