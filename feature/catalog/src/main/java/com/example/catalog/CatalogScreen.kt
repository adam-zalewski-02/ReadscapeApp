package com.example.catalog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    val nfcHandler = NfcHandler(LocalContext.current)
    CatalogScreen(
        onBookClick = onBookClick,
        modifier = modifier,
        catalogState,
        nfcHandler
    )
}

@Composable
internal fun CatalogScreen(
    onBookClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    state: CatalogUiState,
    nfcHandler: NfcHandler
) {
    when(state) {
        is CatalogUiState.Loading -> Loading(modifier)
        is CatalogUiState.Success -> Content(onBookClick,books = state.books, modifier, nfcHandler)
        is CatalogUiState.Error -> Text("No books added")
    }
}

@Composable
internal fun Content(
    onBookClick: (String) -> Unit,
    books: List<Volume>,
    modifier: Modifier = Modifier,
    nfcHandler: NfcHandler
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {
            // Button for preparing data to be sent via HCE
            Button(onClick = {
                nfcHandler.setHceData("Your Custom String")
                // This will set the data in your HCE service to be sent when an NFC reader queries
            }) {
                Text("Prepare Data for NFC")
            }
        }
        item {
            // Button for starting the NFC reader mode
            Button(onClick = {
                nfcHandler.startNfcReaderMode()
                // This will start the NFC reader mode to read data from an NFC tag or HCE device
            }) {
                Text("Receive via NFC")
            }}
        items(books) { book ->
            BookItem(
                book = book,
                onBookClick = {onBookClick(book.id)}
            )
        }
    }
}

