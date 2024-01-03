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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.model.book.BookListing

@Composable
fun BookListingsScreen(
    viewModel: BookListingsViewModel = hiltViewModel()
) {
    val state by viewModel.bookListingsState.collectAsState()

    // New UI components for filtering
    Column {
        FilterBar(viewModel::applyFilter)
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
        // ComboBox for selecting filter type
        ComboBox(selectedFilterType, listOf("title", "author", "isbn", "publisher"), onValueChange = {
        selectedFilterType = it
    })

        Spacer(Modifier.width(16.dp))

        // Search Bar for inputting filter query
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
            Text(text = bookListing.title, style = MaterialTheme.typography.titleLarge)
            Text(text = "Author: ${bookListing.authors.joinToString()}", style = MaterialTheme.typography.bodyLarge)
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

