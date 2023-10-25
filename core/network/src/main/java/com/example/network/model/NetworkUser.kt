package com.example.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkUser(
    val id: Int,
    val email: String,

)