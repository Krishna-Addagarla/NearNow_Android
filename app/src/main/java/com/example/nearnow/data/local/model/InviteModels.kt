package com.example.nearnow.data.local.model

/**
 * Short-term active invitation categories, completely distinct from user interests.
 */
enum class InviteCategory {
    COFFEE,
    FOOD,
    MOVIE,
    WALK
}

/**
 * Represents an active, 6-hour expiring invitation posted by a user.
 */
data class DiscoveryInvite(
    val id: String,
    val user: DiscoveryUser,
    val title: String,
    val description: String,
    val category: InviteCategory,
    val distanceMeters: Int,
    val repliesCount: Int,
    val postTimeAgoText: String = "Heading there in 30 min.",
    val relativeAgeText: String = "10m ago" // Added for UX expiry visibility
) {
    companion object {
        val mockInvites = listOf(
            DiscoveryInvite(
                id = "i1",
                user = DiscoveryUser.mockUsers.first { it.name == "Priya" },
                title = "Coffee near Banjara Hills?",
                description = "Heading there in 30 min.",
                category = InviteCategory.COFFEE,
                distanceMeters = 328,
                repliesCount = 3,
                postTimeAgoText = "Heading there in 30 min.",
                relativeAgeText = "12m ago"
            ),
            DiscoveryInvite(
                id = "i2",
                user = DiscoveryUser.mockUsers.first { it.name == "Karthik" },
                title = "Movie partner tonight?",
                description = "Looking to watch the late show at PVR. I'll get the popcorn!",
                category = InviteCategory.MOVIE,
                distanceMeters = 488,
                repliesCount = 1,
                postTimeAgoText = "Heading there in 2 hours.",
                relativeAgeText = "45m ago"
            ),
            DiscoveryInvite(
                id = "i3",
                user = DiscoveryUser.mockUsers.first { it.name == "Sneha" },
                title = "Quick lunch bite near Road 10?",
                description = "Craving some ramen or sushi. Let's head out soon!",
                category = InviteCategory.FOOD,
                distanceMeters = 420,
                repliesCount = 0,
                postTimeAgoText = "Heading there in 15 min.",
                relativeAgeText = "3m ago"
            )
        )
    }
}
