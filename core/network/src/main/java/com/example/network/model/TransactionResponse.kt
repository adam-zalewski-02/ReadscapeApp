package com.example.network.model

import com.example.model.Transaction

data class TransactionResponse (
    val success: Boolean,
    val transaction: Transaction
)