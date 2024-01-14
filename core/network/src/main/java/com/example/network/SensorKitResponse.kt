package com.example.network

data class SensorKitResponse(
    val _id: String,
    val kit_id: String,
    val condition: String,
    val light: String,
    val temperature: Int,
    val timestamp: String
)