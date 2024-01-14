package com.example.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.book.BookListing

@Composable
fun BorrowLendStatus(bookListing: BookListing) {
    Column {
        if (bookListing.canBeBorrowed) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Book, contentDescription = "Borrowable", tint = Color.Green)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Available to Borrow", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Book, contentDescription = "Not Borrowable", tint = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Not Available to Borrow", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (bookListing.canBeSold) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AttachMoney, contentDescription = "For Sale", tint = Color.Blue)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Available to Sell", fontWeight = FontWeight.Bold)
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AttachMoney, contentDescription = "Not For Sale", tint = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Not Available for Sale", fontWeight = FontWeight.Bold)
            }
        }
    }
}
