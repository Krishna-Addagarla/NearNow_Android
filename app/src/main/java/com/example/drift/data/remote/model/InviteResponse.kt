package com.example.drift.data.remote.model

/**
 * Remote DTO representing an invitation received from the API.
 */
data class InviteResponse(
    val inviteId: String,
    val senderUserId: String,
    val inviteTitle: String,
    val inviteDescription: String,
    val inviteCategory: String, // "COFFEE", "FOOD", "MOVIE", "WALK"
    val userDistanceMeters: Int,
    val repliesTotal: Int,
    val expiryTimestamp: Long,
    val createdTimestamp: Long
)
