package com.example.model

import java.util.Date

data class Transaction (
    val fromUser: String,
    val toUser: String,
    val isbn: String,
    val transactionType: String,
    val lendDate: String,
    val duration: Int,
    val _id: String,
)