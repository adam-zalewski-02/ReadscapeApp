package com.example.model

import com.example.model.book.Volume

data class SearchResult (
    val volumes: List<Volume> = emptyList(),
)