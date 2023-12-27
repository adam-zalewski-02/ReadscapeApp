package com.example.model

import com.example.model.book.Volume

/** An entity that holds the search result */
data class SearchResult (
    val volumes: List<Volume> = emptyList(),
)