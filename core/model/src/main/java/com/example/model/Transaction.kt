package com.example.model

import java.util.Date

data class Transaction (
    val fromUser: String,
    val toUser: String,
    val isbn: String,
    val transactionType: String,
    val lendDate: String? = null,
    val sellDate: String? = null,
    val duration: Int? = null,
    val _id: String,
)