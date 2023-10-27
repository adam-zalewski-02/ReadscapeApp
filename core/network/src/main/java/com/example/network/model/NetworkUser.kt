package com.example.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkUser(
    val _id: String,
    val email: String,
    val password: String,
    val registrationDate: Long
)