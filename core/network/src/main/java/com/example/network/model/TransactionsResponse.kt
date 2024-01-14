package com.example.network.model

import com.example.model.Transaction

data class TransactionsResponse (
    val success: Boolean,
    val transactions: List<Transaction>
)