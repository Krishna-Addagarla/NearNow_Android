package com.example.nearnow.data.local.model

/**
 * Domain-like local model representing a user in the discovery feed/map.
 */
data class DiscoveryUser(
    val id: String,
    val name: String,
    val age: Int,
    val distanceMeters: Int,
    val bio: String,
    val hasActiveStory: Boolean,
    val storyTimeAgo: String? = null,
    val hasInvite: Boolean = false,
    val avatarColorHex: String = "#FFB000",
    val initials: String = "",
    // Position coordinates relative to map center (-1.0f to 1.0f)
    val mapX: Float = 0f,
    val mapY: Float = 0f,
    val photoUrls: List<String> = emptyList(),
    val interests: List<String> = emptyList(),
    val isOnline: Boolean = false,
    val isVerified: Boolean = false
) {
    companion object {
        val mockUsers = listOf(
            DiscoveryUser(
                id = "1",
                name = "Arjun",
                age = 26,
                distanceMeters = 180,
                bio = "Looking for someone to explore Jubilee Hills tonight — coffee or walk?",
                hasActiveStory = true,
                storyTimeAgo = "22m ago",
                hasInvite = false,
                avatarColorHex = "#FFB000", // Mango
                initials = "AR",
                mapX = 0.6f,
                mapY = -0.2f,
                photoUrls = listOf("photo_arjun_1"),
                interests = listOf("COFFEE", "MUSIC", "RUNNING"),
                isOnline = true,
                isVerified = true
            ),
            DiscoveryUser(
                id = "2",
                name = "Sneha",
                age = 23,
                distanceMeters = 420,
                bio = "UX researcher. Love discovering hidden spots in the city and grabbing bubble tea.",
                hasActiveStory = false,
                hasInvite = true,
                avatarColorHex = "#FF6B4A", // Coral
                initials = "SK",
                mapX = -0.4f,
                mapY = 0.5f,
                photoUrls = listOf("photo_sneha_1", "photo_sneha_2"),
                interests = listOf("BOARDS", "ART", "COFFEE", "TRAVEL"),
                isOnline = true,
                isVerified = false
            ),
            DiscoveryUser(
                id = "3",
                name = "Karthik",
                age = 28,
                distanceMeters = 650,
                bio = "Engineer - Movies, food, gaming. Let's grab some pizza and discuss sci-fi movies.",
                hasActiveStory = false,
                hasInvite = false,
                avatarColorHex = "#25EBE1", // Teal
                initials = "KM",
                mapX = -0.2f,
                mapY = -0.6f,
                photoUrls = emptyList(),
                interests = listOf("FOOD", "TRAVEL", "MUSIC"),
                isOnline = false,
                isVerified = true
            ),
            DiscoveryUser(
                id = "4",
                name = "Priya",
                age = 25,
                distanceMeters = 190,
                bio = "Cafe hopping and reading books. Ask me for recommendations!",
                hasActiveStory = true,
                storyTimeAgo = "1h ago",
                hasInvite = false,
                avatarColorHex = "#FFA000", // Mango dark
                initials = "PR",
                mapX = 0.62f,
                mapY = -0.23f,
                photoUrls = listOf("photo_priya_1"),
                interests = listOf("COFFEE", "PHOTOGRAPHY", "ART"),
                isOnline = true,
                isVerified = true
            ),
            DiscoveryUser(
                id = "5",
                name = "Rohan",
                age = 27,
                distanceMeters = 200,
                bio = "Fitness enthusiast, runner, morning coffee lover.",
                hasActiveStory = true,
                storyTimeAgo = "2h ago",
                hasInvite = false,
                avatarColorHex = "#FFB000",
                initials = "RN",
                mapX = 0.58f,
                mapY = -0.18f,
                photoUrls = emptyList(),
                interests = listOf("RUNNING", "CYCLING", "COFFEE"),
                isOnline = false,
                isVerified = false
            ),
            DiscoveryUser(
                id = "6",
                name = "Divya",
                age = 24,
                distanceMeters = 300,
                bio = "Music listener and amateur guitarist. Jamming sessions are always welcome.",
                hasActiveStory = false,
                hasInvite = true,
                avatarColorHex = "#FF6B4A",
                initials = "DS",
                mapX = 0.1f,
                mapY = 0.7f,
                photoUrls = emptyList(),
                interests = listOf("MUSIC", "TREKKING", "TRAVEL"),
                isOnline = true,
                isVerified = false
            )
        )
    }
}
