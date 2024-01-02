package com.example.model.book

data class BookListing(
    val _id: String,
    val pageCount: Int,
    val thumbnailLink: String,
    val canBeBorrowed: Boolean,
    val canBeSold: Boolean,
    val authors: List<String>,
    val keywords: List<String>,
    val extraInfoFromOwner: String,
    val maturityRating: String,
    val ownerId: String,
    val isbn: String,
    val title: String,
    val language: String,
    val publisher: String,
    val categories: List<String>,
    val publishedDate: String,
    val description: String,
    val similarBooks: List<String>,
    val published_at: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int,
    val id: String
)