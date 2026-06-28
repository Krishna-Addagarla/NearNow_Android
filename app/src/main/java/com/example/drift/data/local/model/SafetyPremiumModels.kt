package com.example.drift.data.local.model

/**
 * Categories describing safety report violations.
 */
enum class SafetyReportReason(val displayTitle: String) {
    HARASSMENT("Harassment or threats"),
    INAPPROPRIATE_PHOTOS("Inappropriate photos"),
    FAKE_PROFILE("Fake profile"),
    OTHER("Something else")
}

/**
 * Pricing subscription tier options for the premium paywall.
 */
data class PremiumTier(
    val id: String,
    val name: String, // e.g. "MONTHLY" or "3 MONTHS"
    val priceText: String, // e.g. "₹399" or "₹249"
    val billedDetails: String, // e.g. "billed monthly" or "billed as ₹747" (UX recommendation)
    val durationMonths: Int,
    val isRecommended: Boolean = false
) {
    companion object {
        val mockTiers = listOf(
            PremiumTier(
                id = "t1",
                name = "MONTHLY",
                priceText = "₹399",
                billedDetails = "billed monthly",
                durationMonths = 1,
                isRecommended = false
            ),
            PremiumTier(
                id = "t2",
                name = "3 MONTHS",
                priceText = "₹249",
                billedDetails = "billed as ₹747",
                durationMonths = 3,
                isRecommended = true
            )
        )
    }
}
