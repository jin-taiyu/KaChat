package com.kachat.models

@kotlinx.serialization.Serializable
data class Message(
    val id: Int,
    val senderId: Int,
    val recipientId: Int?,
    val content: String,
    val roomType: String,
    val createTime: String
)
