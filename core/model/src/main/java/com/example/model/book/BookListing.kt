package com.example.model.book

data class BookListing(
    val _id: String,
    var pageCount: Int,
    var thumbnailLink: String,
    val canBeBorrowed: Boolean,
    val canBeSold: Boolean,
    var authors: List<String>,
    val keywords: List<String>,
    val extraInfoFromOwner: String,
    val maturityRating: String,
    val ownerId: String,
    val isbn: String,
    var title: String,
    var language: String,
    var publisher: String,
    var categories: List<String>,
    var publishedDate: String,
    var description: String,
    val similarBooks: List<String>,
    val published_at: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int,
    val id: String
)