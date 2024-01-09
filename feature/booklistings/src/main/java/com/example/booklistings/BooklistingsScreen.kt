package com.example.booklistings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.model.book.BookListing

@Composable
fun BookListingsScreen(viewModel: BookListingsViewModel = hiltViewModel()) {
    val state by viewModel.bookListingsState.collectAsState()
    val selectedBookListing by viewModel.selectedBookListing.collectAsState()

    if (selectedBookListing != null) {
        BookListingDetailScreen(bookListing = selectedBookListing!!, onBack = viewModel::clearSelectedBookListing)
    } else {
        when (state) {
            is BookListingsState.Loading -> {
                // Display loading UI
            }
            is BookListingsState.Success -> {
                val bookListings = (state as BookListingsState.Success).bookListings
                Column {
                    FilterBar(onFilterApplied = viewModel::applyFilter)
                    Spacer(Modifier.height(16.dp))
                    BookListingsList(bookListings, viewModel::selectBookListing)
                }
            }
            is BookListingsState.Error -> {
                // Display error UI
            }
        }
    }
}

@Composable
fun FilterBar(onFilterApplied: (Map<String, String>) -> Unit) {
    var selectedFilterType by remember { mutableStateOf("title") }
    var searchText by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ComboBox(selectedFilterType, listOf("title", "authors", "isbn", "publisher"), onValueChange = {
        selectedFilterType = it
    })

        Spacer(Modifier.width(16.dp))
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search") },
            modifier = Modifier.weight(1f)
        )

        Button(onClick = {
            onFilterApplied(mapOf(selectedFilterType to searchText))
        }) {
            Text("Search")
        }
    }
}

@Composable
fun BookListingsList(bookListings: List<BookListing>, onSelectBook: (BookListing) -> Unit) {
    LazyColumn {
        items(bookListings) { bookListing ->
            BookListItem(bookListing, onSelectBook)
        }
    }
}

@Composable
fun BookListItem(bookListing: BookListing, onSelectBook: (BookListing) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onSelectBook(bookListing) },
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
            Text(text = bookListing.title, style = MaterialTheme.typography.titleLarge)
            Text(text = "Author: ${bookListing.authors.joinToString()}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}


@Composable
fun BookListingDetailScreen(
    bookListing: BookListing,
    onBack: () -> Unit,
    viewModel: BookListingsViewModel = hiltViewModel()
) {
    val ownerId = bookListing.ownerId // Replace with the actual ID field
    val ownerEmail by viewModel.ownerEmail.collectAsState()

    LaunchedEffect(ownerId) {
        viewModel.fetchOwnerEmail(ownerId)
    }
    val similarBookListings by viewModel.getBookListingsByIds(bookListing.similarBooks).collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = onBack) {
            Text("Back")
        }
        Spacer(Modifier.height(16.dp))
        Text(text = bookListing.title, style = MaterialTheme.typography.headlineMedium)
        Text(text = "Author(s): ${bookListing.authors.joinToString()}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Description: ${bookListing.description}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Page Count: ${bookListing.pageCount}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Language: ${bookListing.language}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Publisher: ${bookListing.publisher}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Published Date: ${bookListing.publishedDate}", style = MaterialTheme.typography.bodySmall)
        Text(text = "ISBN: ${bookListing.isbn}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Maturity Rating: ${bookListing.maturityRating}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Extra Info: ${bookListing.extraInfoFromOwner}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Keywords: ${bookListing.keywords.joinToString()}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Categories: ${bookListing.categories.joinToString()}", style = MaterialTheme.typography.bodySmall)

        if (bookListing.canBeBorrowed) {
            Text(text = "Available for Borrowing", color = Color.Green, style = MaterialTheme.typography.bodyLarge)
        }
        if (bookListing.canBeSold) {
            Text(text = "Available for Sale", color = Color.Blue, style = MaterialTheme.typography.bodyLarge)
        }
        Text(text = "Owner's Email: $ownerEmail", style = MaterialTheme.typography.bodyMedium)

        if (similarBookListings.isNotEmpty()) {
            Text("Similar Books", style = MaterialTheme.typography.headlineMedium)
            LazyColumn {
                items(similarBookListings) { similarBookListing ->
                    BookListItem(
                        bookListing = similarBookListing,
                        onSelectBook = { selectedBook -> viewModel.selectBookListing(selectedBook) }
                    )
                }
            }
        }
    }
}

@Composable
fun ComboBox(selectedFilterType: String, items: List<String>, onValueChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
            .clickable(onClick = { expanded = true })
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(selectedFilterType)
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "Dropdown Arrow"
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { label ->
                DropdownMenuItem(
                    onClick = {
                        onValueChange(label)
                        expanded = false
                    },
                    text = { Text(text = label) }
                )
            }
        }
    }
}

