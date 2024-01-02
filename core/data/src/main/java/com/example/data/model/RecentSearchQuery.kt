package com.example.data.model

import com.example.database.model.RecentSearchQueryEntity
import kotlinx.datetime.Clock

data class RecentSearchQuery(
    val query: String,
    val queriedDate: kotlinx.datetime.Instant = Clock.System.now(),
)

fun RecentSearchQueryEntity.asExternalModel() = RecentSearchQuery(
    query = query,
    queriedDate = queriedDate,
)