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
    val avatarColorHex: String = "#8B96A8",
    val initials: String = "",
    // Position coordinates relative to map center (-1.0f to 1.0f)
    val mapX: Float = 0f,
    val mapY: Float = 0f
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
                avatarColorHex = "#00E6A8", // Signal color
                initials = "AR",
                mapX = 0.6f,
                mapY = -0.2f
            ),
            DiscoveryUser(
                id = "2",
                name = "Sneha",
                age = 23,
                distanceMeters = 420,
                bio = "UX researcher. Love discovering hidden spots in the city and grabbing bubble tea.",
                hasActiveStory = false,
                hasInvite = true,
                avatarColorHex = "#FF6B4A", // Coral color
                initials = "SK",
                mapX = -0.4f,
                mapY = 0.5f
            ),
            DiscoveryUser(
                id = "3",
                name = "Karthik",
                age = 28,
                distanceMeters = 650,
                bio = "Engineer - Movies, food, gaming. Let's grab some pizza and discuss sci-fi movies.",
                hasActiveStory = false,
                hasInvite = false,
                avatarColorHex = "#8B96A8", // Slate color
                initials = "KM",
                mapX = -0.2f,
                mapY = -0.6f
            ),
            // Let's add a couple more mock users for rich spatial distribution & clustering demo
            DiscoveryUser(
                id = "4",
                name = "Priya",
                age = 25,
                distanceMeters = 190,
                bio = "Cafe hopping and reading books. Ask me for recommendations!",
                hasActiveStory = true,
                storyTimeAgo = "1h ago",
                hasInvite = false,
                avatarColorHex = "#00E6A8",
                initials = "PR",
                // Clustered near Arjun (0.6f, -0.2f) to demonstrate clustering
                mapX = 0.62f,
                mapY = -0.23f
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
                avatarColorHex = "#00E6A8",
                initials = "RN",
                // Clustered near Arjun and Priya to trigger "+2" or more badge
                mapX = 0.58f,
                mapY = -0.18f
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
                mapY = 0.7f
            )
        )
    }
}
