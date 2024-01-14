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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.material3.ButtonDefaults
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.NfcHandler
import com.example.model.NfcReceivedDataManager
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
    val nfcHandler = NfcHandler(LocalContext.current)
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
        },
        nfcHandler = nfcHandler,
        onLendOut = viewModel::lendOutBook
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
    nfcHandler: NfcHandler,
    onViewBookListingClick: (String) -> Unit,
    onLendOut: (String, String, String) -> Unit,
) {

    when (state) {
        is BookDetailUiState.Loading -> Loading(modifier)
        is BookDetailUiState.Success -> Content(
            volume = state.volume,
            modifier = modifier,
            onAddToFavoritesClick = onAddToFavoritesClick,
            onBack = onBack,
            onPublishBookListingClick = onPublishBookListingClick,
            bookListingExists = bookListingExists,
            onViewBookListingClick = onViewBookListingClick,
            nfcHandler,
            onLendOut = onLendOut
        )
        is BookDetailUiState.Error -> Text("Error")
        is BookDetailUiState.EditBookListing -> EditBookListingScreen(
            bookListing = state.bookListing,
            onSaveClicked = onEditListing,
            onBackClicked = onBack
        )
        is BookDetailUiState.ViewBookListing -> ViewBookListingScreen(
            bookListing = state.bookListing,
            onBackClicked = onBack,
            onEditListing = onEditListing,
            onDeleteClicked = onDeleteClicked
        )
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

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            Text("Edit Book Listing", fontWeight = FontWeight.Bold)
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            OutlinedTextField(
                value = extraInfoFromOwner,
                onValueChange = { extraInfoFromOwner = it },
                label = { Text("Your description for the book") }
            )
        }
        item {
            OutlinedTextField(
                value = categories,
                onValueChange = { categories = it },
                label = { Text("Categories (comma separated)") }
            )
        }
        item {
            OutlinedTextField(
                value = keywords,
                onValueChange = { keywords = it },
                label = { Text("Keywords (comma separated)") }
            )
        }
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Can be Borrowed")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(checked = canBeBorrowed, onCheckedChange = { canBeBorrowed = it })
            }
        }
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Can be Sold")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(checked = canBeSold, onCheckedChange = { canBeSold = it })
            }
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = onBackClicked) {
                    Text("Cancel")
                }

                Spacer(modifier = Modifier.weight(1f))

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

            }
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
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClicked) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = { isEditing = true }) {
                    Text("Edit Listing")
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(
                    listOf(
                        "Title" to bookListing.title,
                        "Author(s)" to bookListing.authors.joinToString(),
                        "ISBN" to bookListing.isbn,
                        "Publisher" to bookListing.publisher,
                        "Published Date" to bookListing.publishedDate,
                        "Language" to bookListing.language,
                        "Categories" to bookListing.categories.joinToString(),
                        "Keywords" to bookListing.keywords.joinToString(),
                        "Page Count" to bookListing.pageCount.toString(),
                        "Maturity Rating" to bookListing.maturityRating,
                        "Extra Info From Owner" to bookListing.extraInfoFromOwner
                    )
                ) { detail ->
                    detail.second?.let { BookDetail(detail.first, it) }
                }
                item {
                    BorrowLendStatus(bookListing)
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Button(
                        onClick = { onDeleteClicked(bookListing.isbn) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    ) {
                        Text("Delete Listing")
                    }
                }
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
    onViewBookListingClick: (String) -> Unit,
    nfcHandler: NfcHandler,
    onLendOut: (String, String, String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        item {
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
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item {
            IconButton(onClick = { onBack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        }

        item {
            Text(
                text = volume.volumeInfo.title,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        }

        volume.volumeInfo.authors?.let { authors ->
            item {
                Text(
                    text = authors.joinToString(", "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        volume.volumeInfo.publishedDate?.let { publishedDate ->
            item {
                Text(
                    text = "Published Date: $publishedDate",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        volume.volumeInfo.description?.let { description ->
            item {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        item {
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
        }

        item {
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
    Button(
        onClick = {
            nfcHandler.startNfcReaderMode()
        }
    ) {
        Text(text = "Lend out")
    }

    LaunchedEffect(NfcReceivedDataManager.getData()) {
        NfcReceivedDataManager.getData()?.let { volume.volumeInfo.industryIdentifiers?.firstOrNull()
            ?.let { it1 -> onLendOut(it.data, volume.id, it1.identifier) } }
        nfcHandler.stopNfcReaderMode()
    }
}