package com.example.nearnow.data.remote.model

/**
 * Remote DTO (Data Transfer Object) for individual chat message.
 */
data class ChatMessageResponse(
    val messageId: String,
    val senderUserId: String,
    val recipientUserId: String,
    val messageContent: String,
    val sentTimestamp: Long,
    val deliveryStatus: String
)

/**
 * Remote DTO representing a chat session/conversation thread.
 */
data class ChatSessionResponse(
    val threadId: String,
    val participantUserId: String,
    val threadStatus: String, // "active", "paused", "permanent", etc.
    val latestMessage: ChatMessageResponse?,
    val dailyLimitUsed: Int,
    val dailyLimitMax: Int
)
