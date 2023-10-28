package com.example.network.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val success: Boolean,
    val user: NetworkUser
)