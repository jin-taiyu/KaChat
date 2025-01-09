package com.kachat.dto

@kotlinx.serialization.Serializable
data class MessageDto(
    val senderName: String,
    val recipientName: String? = null,
    val content: String,
    val roomType: String
)
