package com.example.nearnow.data.remote.model

/**
 * Remote DTO (Data Transfer Object) representing a user received from the remote API.
 */
data class DiscoveryUserResponse(
    val id: String,
    val name: String,
    val age: Int,
    val distance: Int,
    val description: String,
    val activeStoryUrl: String? = null,
    val storyTimestamp: Long? = null,
    val activeInviteId: String? = null,
    val profileColorHex: String? = null,
    val initials: String? = null,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
