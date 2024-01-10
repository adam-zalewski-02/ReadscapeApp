package com.example.bookdetail

import android.util.Log
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
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.model.book.Volume
import com.example.designsystem.component.Loading
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.icon.ReadscapeIcons
import com.example.model.book.BookListing
import androidx.compose.material3.ButtonDefaults
import com.example.ui.BookDetail
import com.example.ui.BorrowLendStatus

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
        onBack = viewModel::returnToBookDetailScreen,
        onPublishBookListingClick = { isbn ->
            viewModel.publishBookListing(isbn)
        },
        onEditListing = { updatedBookListing ->
            viewModel.updateBookListing(updatedBookListing)
        },
        onViewBookListingClick = { isbn ->
            viewModel.viewBookListing(isbn)
        },
        bookListingExists = bookListingExists,
        onDeleteClicked = { isbn ->
            viewModel.deleteBookListing(isbn)
        }
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
    onDeleteClicked: (String) -> Unit,
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
                    onSaveClicked = onEditListing,
                    onBackClicked = onBack
                )
            }
            is BookDetailUiState.ViewBookListing -> item {
                ViewBookListingScreen(
                    bookListing = state.bookListing,
                    onBackClicked = onBack,
                    onEditListing = onEditListing,
                    onDeleteClicked = onDeleteClicked
                )
            }
        }
    }
}

@Composable
fun EditBookListingScreen(
    bookListing: BookListing,
    onSaveClicked: (BookListing) -> Unit,
    onBackClicked: () -> Unit
) {
    var canBeBorrowed by remember { mutableStateOf(bookListing.canBeBorrowed) }
    var canBeSold by remember { mutableStateOf(bookListing.canBeSold) }
    var extraInfoFromOwner by remember { mutableStateOf(bookListing.extraInfoFromOwner) }
    var categories by remember { mutableStateOf(bookListing.categories.joinToString()) }
    var keywords by remember { mutableStateOf(bookListing.keywords.joinToString()) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Edit Book Listing", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = extraInfoFromOwner,
            onValueChange = { extraInfoFromOwner = it },
            label = { Text("Your description for the book") }
        )
        OutlinedTextField(
            value = categories,
            onValueChange = { categories = it },
            label = { Text("Categories (comma separated)") }
        )
        OutlinedTextField(
            value = keywords,
            onValueChange = { keywords = it },
            label = { Text("Keywords (comma separated)") }
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Can be Borrowed")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(checked = canBeBorrowed, onCheckedChange = { canBeBorrowed = it })
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Can be Sold")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(checked = canBeSold, onCheckedChange = { canBeSold = it })
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val updatedBookListing = bookListing.copy(
                extraInfoFromOwner = extraInfoFromOwner,
                categories = categories.split(",").map { it.trim() },
                keywords = keywords.split(",").map { it.trim() },
                canBeBorrowed = canBeBorrowed,
                canBeSold = canBeSold
            )
            Log.d("EditBookScreen", "Book listing to be saved: $updatedBookListing")
            onSaveClicked(updatedBookListing)
        }) {
            Text("Save")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onBackClicked) {
            Text("Back")
        }
    }
}

@Composable
fun ViewBookListingScreen(
    bookListing: BookListing,
    onBackClicked: () -> Unit,
    onEditListing: (BookListing) -> Unit,
    onDeleteClicked: (String) -> Unit,
) {
    var isEditing by remember { mutableStateOf(false) }

    if (isEditing) {
        EditBookListingScreen(
            bookListing = bookListing,
            onSaveClicked = { updatedBookListing ->
                onEditListing(updatedBookListing)
                isEditing = false
            },
            onBackClicked = {
                isEditing = false
            }
        )
    } else {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Button(onClick = { onBackClicked() }) {
                    Text("Back")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { isEditing = true }) {
                    Text("Edit Listing")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            BookDetail("Title", bookListing.title)
            BookDetail("Author(s)", bookListing.authors.joinToString())
            BookDetail("ISBN", bookListing.isbn)
            BookDetail("Language", bookListing.language)
            BookDetail("Publisher", bookListing.publisher)
            BookDetail("Published Date", bookListing.publishedDate ?: "Not Available")
            BookDetail("Page Count", bookListing.pageCount.toString())
            BookDetail("Categories", bookListing.categories.joinToString())
            BookDetail("Keywords", bookListing.keywords.joinToString())
            BookDetail("Maturity Rating", bookListing.maturityRating)
            BookDetail("Your Description", bookListing.extraInfoFromOwner)
            BorrowLendStatus(bookListing)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onDeleteClicked(bookListing.isbn) },
                        colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error, // Red color for delete button
                contentColor = MaterialTheme.colorScheme.onError // Ensures text color is visible on red background
            )
            ) {
                Text("Delete Listing")
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