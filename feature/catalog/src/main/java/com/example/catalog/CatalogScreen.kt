package com.example.catalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
    var nfcMessageToSend by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = nfcMessageToSend,
                onValueChange = { nfcMessageToSend = it },
                label = { Text("NFC Message to Send") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = { nfcHandler.setHceData(nfcMessageToSend) },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Send via NFC")
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { nfcHandler.startNfcReaderMode() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Receive via NFC")
            }
            TextField(
                value = nfcHandler.receivedNfcData,
                onValueChange = {},
                label = { Text("Received NFC Data") },
                readOnly = true,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
        }

        Button(onClick = { nfcHandler.stopNfcReaderMode() }) {
            Text("Stop NFC Reader Mode")
        }

        // Your existing LazyColumn for books
        LazyColumn {
            items(books) { book ->
                BookItem(
                    book = book,
                    onBookClick = { onBookClick(book.id) }
                )
            }
        }
    }
}


