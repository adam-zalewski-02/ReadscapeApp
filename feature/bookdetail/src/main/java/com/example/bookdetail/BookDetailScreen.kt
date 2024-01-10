package com.example.bookdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.model.book.Volume
import com.example.designsystem.component.Loading
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.icon.ReadscapeIcons
import com.example.model.book.BookListing

@Composable
internal fun BookDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: BookDetailViewModel = hiltViewModel(),
    volumeId: String,
    onBack: () -> Unit,
) {
    val bookDetailsState by viewModel.bookDetails.collectAsStateWithLifecycle()
    val bookListingExists by viewModel.bookListingExists.collectAsStateWithLifecycle()

    LaunchedEffect(volumeId) {
        viewModel.getBookDetails(volumeId)
    }

    LaunchedEffect(bookDetailsState) {
        if (bookDetailsState is BookDetailUiState.Success) {
            val isbn = (bookDetailsState as BookDetailUiState.Success).volume.volumeInfo.industryIdentifiers?.firstOrNull()?.identifier
            isbn?.let { viewModel.checkIfBookListingExists(it) }
        }
    }

    BookDetailScreen(
        modifier = modifier,
        state = bookDetailsState,
        onAddToFavoritesClick = viewModel::addBookToFavorites,
        onBack = onBack,
        onPublishBookListingClick = { isbn ->
            viewModel.publishBookListing(isbn) // This function needs to be implemented in the ViewModel
        },
        onEditListing = { updatedBookListing ->
            viewModel.updateBookListing(updatedBookListing)
        },
        onViewBookListingClick = { isbn ->
            viewModel.viewBookListing(isbn)
        },
        bookListingExists = bookListingExists,
    )
}


@Composable
fun BookDetailScreen(
    modifier: Modifier = Modifier,
    state: BookDetailUiState,
    onAddToFavoritesClick: (String) -> Unit,
    onBack: () -> Unit,
    onPublishBookListingClick: (String) -> Unit,
    bookListingExists: Boolean,
    onEditListing: (BookListing) -> Unit,
    onViewBookListingClick: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        when (state) {
            is BookDetailUiState.Loading -> item {
                Loading(modifier)
            }
            is BookDetailUiState.Success -> item {
                Content(
                    volume = state.volume,
                    modifier = modifier,
                    onAddToFavoritesClick = onAddToFavoritesClick,
                    onBack = onBack,
                    onPublishBookListingClick = onPublishBookListingClick,
                    bookListingExists = bookListingExists,
                    onViewBookListingClick = onViewBookListingClick
                )
            }
            is BookDetailUiState.Error -> item { Text("Error") }
            is BookDetailUiState.EditBookListing -> item {
                EditBookListingScreen(
                    bookListing = state.bookListing,
                    onSaveClicked = { /* Your save logic here */ },
                    onBackClicked = onBack
                )
            }
            is BookDetailUiState.ViewBookListing -> item {
                ViewBookListingScreen(
                    bookListing = state.bookListing,
                    onBackClicked = onBack,
                    onEditListing = onEditListing
                )
            }
        }
    }
}


@Composable
fun EditBookListingScreen(
    bookListing: BookListing,
    onSaveClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    // Implement the UI for editing a book listing here
    // This is just a placeholder, replace with your actual UI
    Column {
        Text("Edit Book Listing for ${bookListing.title}")
        // Add form fields for editing the book listing here
        Button(onClick = onSaveClicked) {
            Text("Save")
        }
    }
}

@Composable
fun ViewBookListingScreen(
    bookListing: BookListing,
    onBackClicked: () -> Unit,
    onEditListing: (BookListing) -> Unit // Callback to handle edit action
) {
    var isEditing by remember { mutableStateOf(false) }

    if (isEditing) {
        // Render the editing interface
        EditBookListingScreen(
            bookListing = bookListing,
            onSaveClicked = {
                // Save the edited book listing
                onEditListing(bookListing)
                isEditing = false // Switch back to view mode after saving
            },
            onBackClicked = {
                isEditing = false // Switch back to view mode without saving
            }
        )
    } else {
        // Render the viewing interface
        Column {
            Text("Viewing Book Listing for ${bookListing.title}")
            // Display the details of the book listing here
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { onBackClicked() }) {
                Text("Back")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { isEditing = true }) {
                Text("Edit Listing")
            }
        }
    }
}


@Composable
internal fun Content(
    volume: Volume,
    modifier: Modifier = Modifier,
    onAddToFavoritesClick: (String) -> Unit,
    onBack: () -> Unit,
    onPublishBookListingClick: (String) -> Unit,
    bookListingExists: Boolean,
    onViewBookListingClick: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Book Image
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(volume.volumeInfo.imageLinks?.thumbnail)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))

        IconButton(
            onClick = { onBack() }
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
        }
        // Book Details
        Text(
            text = volume.volumeInfo.title,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        volume.volumeInfo.authors?.let {
            Text(
                text = it.joinToString(", "),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        volume.volumeInfo.publishedDate?.let {
            Text(
                text = "Published Date: $it",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        volume.volumeInfo.description?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Add to Favorites Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { onAddToFavoritesClick(volume.id) },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(48.dp)
            ) {
                Icon(imageVector = ReadscapeIcons.BookmarkBorder, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Add to collection")
            }
        }
        if (bookListingExists) {
            Button(
                onClick = { onViewBookListingClick(volume.volumeInfo.industryIdentifiers?.firstOrNull()?.identifier ?: "") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("View Booklisting")
            }
        } else {
            // "Publish Booklisting" button
            Button(
                onClick = { onPublishBookListingClick(volume.volumeInfo.industryIdentifiers?.firstOrNull()?.identifier ?: "") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Publish Booklisting")
            }
        }
    }
}