package com.kachat.dto

@kotlinx.serialization.Serializable
data class RegisterRequest(
    val username: String,
    val password: String
)
