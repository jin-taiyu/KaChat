package com.kachat.dto

@kotlinx.serialization.Serializable
data class LoginRequest(
    val username: String,
    val password: String
)
