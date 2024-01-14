package com.example.network.model

import kotlinx.serialization.Serializable

@Serializable
data class TransactionRequest (
    val userId: String,
    val toUserId: String,
    val isbn: String,
    val duration: Int,
)