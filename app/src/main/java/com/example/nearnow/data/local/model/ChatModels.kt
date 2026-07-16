package com.example.nearnow.data.local.model

/**
 * Represents the status of a chat session.
 */
enum class ChatSessionStatus {
    ACTIVE,
    PAUSED,
    REQUEST,
    PERMANENT
}

/**
 * Represents an individual chat message in a session.
 */
data class ChatMessage(
    val id: String,
    val senderId: String, // "me" or target user ID
    val text: String,
    val timestampText: String, // e.g. "Today 4:22 PM"
    val isOutgoing: Boolean
)

/**
 * Represents a conversation between the current user and a discovery user.
 */
data class ChatSession(
    val id: String,
    val user: DiscoveryUser,
    val messages: List<ChatMessage>,
    val status: ChatSessionStatus,
    val requestCountUsed: Int = 4,
    val requestLimit: Int = 5
) {
    companion object {
        val mockSessions = listOf(
            // Session 1: Active Chat with Arjun
            ChatSession(
                id = "s1",
                user = DiscoveryUser.mockUsers.first { it.name == "Arjun" },
                status = ChatSessionStatus.ACTIVE,
                messages = listOf(
                    ChatMessage(
                        id = "m1",
                        senderId = "1",
                        text = "Hey! Also near Jubilee Hills",
                        timestampText = "4:22 PM",
                        isOutgoing = false
                    ),
                    ChatMessage(
                        id = "m2",
                        senderId = "me",
                        text = "What café did you have in mind?",
                        timestampText = "4:23 PM",
                        isOutgoing = true
                    ),
                    ChatMessage(
                        id = "m3",
                        senderId = "1",
                        text = "Third Wave, Road 10. See you there 😉",
                        timestampText = "4:24 PM",
                        isOutgoing = false
                    )
                )
            ),
            // Session 2: Paused Chat with Sneha
            ChatSession(
                id = "s2",
                user = DiscoveryUser.mockUsers.first { it.name == "Sneha" },
                status = ChatSessionStatus.PAUSED,
                messages = listOf(
                    ChatMessage(
                        id = "m4",
                        senderId = "me",
                        text = "Hey Sneha! Love the UI design work you do.",
                        timestampText = "Yesterday",
                        isOutgoing = true
                    ),
                    ChatMessage(
                        id = "m5",
                        senderId = "2",
                        text = "Hey, thank you so much! That sounds fun!",
                        timestampText = "Yesterday",
                        isOutgoing = false
                    )
                )
            ),
            // Session 3: Permanent Chat with Karthik
            ChatSession(
                id = "s3",
                user = DiscoveryUser.mockUsers.first { it.name == "Karthik" },
                status = ChatSessionStatus.PERMANENT,
                messages = listOf(
                    ChatMessage(
                        id = "m6",
                        senderId = "me",
                        text = "Let's catch up for pizza tonight?",
                        timestampText = "Friday",
                        isOutgoing = true
                    ),
                    ChatMessage(
                        id = "m7",
                        senderId = "3",
                        text = "Sure, I'm down for pizza! See you.",
                        timestampText = "Friday",
                        isOutgoing = false
                    )
                )
            )
        )
    }
}
