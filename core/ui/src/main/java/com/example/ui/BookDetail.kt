package com.example.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun BookDetail(label: String, content: String, isDescription: Boolean = false) {
    Text(text = label, fontWeight = FontWeight.Bold)
    if (isDescription) {
        Text(text = content, maxLines = 50, overflow = TextOverflow.Ellipsis)
    } else {
        Text(text = content)
    }
    Spacer(modifier = Modifier.height(8.dp))
}