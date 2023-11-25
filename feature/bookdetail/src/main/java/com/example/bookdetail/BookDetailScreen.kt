package com.example.bookdetail

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.example.ui.Loading
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun BookDetailRoute(
    modifier: Modifier = Modifier,
    viewmodel: BookDetailViewModel = hiltViewModel(),
    volumeId: String,
    onBack: () -> Unit,
) {
    val bookDetailsState by viewmodel.bookDetails.collectAsStateWithLifecycle()

    LaunchedEffect(volumeId) {
        viewmodel.getBookDetails(volumeId)
    }

    BookDetailScreen(
        modifier = modifier,
        state = bookDetailsState,
        onAddToFavoritesClick = viewmodel::addBookToFavorites,
        onBack = onBack
    )
}

@Composable
fun BookDetailScreen(
    modifier: Modifier = Modifier,
    state: BookDetailUiState,
    onAddToFavoritesClick: (String) -> Unit,
    onBack: () -> Unit
) {

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        when (state) {
            is BookDetailUiState.Loading -> item { Loading(modifier) }
            is BookDetailUiState.Success -> item {
                Content(
                    volume = state.volume,
                    modifier = modifier,
                    onAddToFavoritesClick = onAddToFavoritesClick,
                    onBack = onBack
                )
            }
            is BookDetailUiState.Error -> item { Text("Error") }
        }
    }
}

@Composable
internal fun Content(
    volume: Volume,
    modifier: Modifier = Modifier,
    onAddToFavoritesClick: (String) -> Unit,
    onBack: () -> Unit
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
                Icon(imageVector = Icons.Default.Favorite, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Add to Favorites")
            }
        }
    }
}