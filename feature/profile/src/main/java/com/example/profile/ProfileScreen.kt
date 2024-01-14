package com.example.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.Transaction

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {

    val transactionsResult by viewModel.transactions.collectAsStateWithLifecycle()
    val userEmail by viewModel.userEmail.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        userEmail?.let {
            Text(
                text = it,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Android Developer",
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))

        when {
            transactionsResult.isSuccess -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(transactionsResult.getOrNull()?.transactions ?: emptyList()) { transaction ->
                        TransactionItem(transaction)
                    }
                }
            }
            transactionsResult.isFailure -> {
                Text(text = "Failed to load transactions: ${transactionsResult.exceptionOrNull()?.message ?: "Unknown error"}")
            }
            else -> {
                Text(text = "Loading transactions...")
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Transaction ID: ${transaction._id}")
            Text(text = "From User: ${transaction.fromUser}")
            Text(text = "To User: ${transaction.toUser}")
            Text(text = "ISBN: ${transaction.isbn}")
            transaction.lendDate.let { Text(text = "Lend Date: $it") }
            transaction.duration.let { Text(text = "Duration: $it") }
        }
    }
}

