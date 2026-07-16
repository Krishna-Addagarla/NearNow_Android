package com.example.nearnow.data.remote.model

/**
 * Remote DTO representing safety report submissions.
 */
data class SafetyReportResponse(
    val reportId: String,
    val targetUserId: String,
    val reporterUserId: String,
    val reasonCategory: String,
    val reasonDetailsText: String?,
    val submissionTimestamp: Long
)

/**
 * Remote DTO representing user subscription upgrades.
 */
data class PremiumSubscriptionResponse(
    val transactionId: String,
    val tierId: String,
    val originalPriceBilled: Double,
    val currency: String, // "INR"
    val upgradeTimestamp: Long,
    val expirationTimestamp: Long,
    val autoRenewEnabled: Boolean
)
