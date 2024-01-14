package com.example.model

/**
 * An entity of [SearchResult] with additional user information such as whether the user is
 * following a book.
 */
data class UserSearchResult(
    val volumes: List<FollowableVolume> = emptyList(),
)
