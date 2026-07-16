package com.example.nearnow.data.remote.model

import com.google.gson.annotations.SerializedName

// --- Auth Models ---
data class OtpSendRequest(
    @SerializedName("phone_number") val phoneNumber: String
)

data class OtpSendResponse(
    @SerializedName("sent") val sent: Boolean,
    @SerializedName("dev_otp") val devOtp: String? = null
)

data class OtpVerifyRequest(
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("otp") val otp: String
)

data class TokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("user_id") val userId: Int
)

data class FirebaseVerifyRequest(
    @SerializedName("id_token") val idToken: String
)

// --- Profile Models ---
data class ProfileUpsert(
    @SerializedName("display_name") val displayName: String,
    @SerializedName("birth_year") val birthYear: Int,
    @SerializedName("gender") val gender: String,
    @SerializedName("bio") val bio: String? = null,
    @SerializedName("photo_url") val photoUrl: String? = null,
    @SerializedName("interested_in") val interestedIn: List<String> = emptyList(),
    @SerializedName("min_age") val minAge: Int = 18,
    @SerializedName("max_age") val maxAge: Int = 60,
    @SerializedName("discovery_radius_m") val discoveryRadiusM: Int = 2000
)

data class ProfileResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("birth_year") val birthYear: Int,
    @SerializedName("gender") val gender: String,
    @SerializedName("bio") val bio: String? = null,
    @SerializedName("photo_url") val photoUrl: String? = null,
    @SerializedName("interested_in") val interestedIn: List<String>,
    @SerializedName("min_age") val minAge: Int,
    @SerializedName("max_age") val maxAge: Int,
    @SerializedName("discovery_radius_m") val discoveryRadiusM: Int,
    @SerializedName("is_invisible") val isInvisible: Boolean,
    @SerializedName("created_at") val createdAt: String
)

// --- Discovery Models ---
data class LocationUpdate(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
)

data class RadiusUpdate(
    @SerializedName("radius_m") val radiusM: Int
)

data class InvisibleUpdate(
    @SerializedName("is_invisible") val isInvisible: Boolean
)

data class NearbyProfile(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("age") val age: Int,
    @SerializedName("gender") val gender: String,
    @SerializedName("bio") val bio: String? = null,
    @SerializedName("photo_url") val photoUrl: String? = null,
    @SerializedName("distance_band") val distanceBand: String
)

data class NearbyResponse(
    @SerializedName("users") val users: List<NearbyProfile>
)

// --- Chat Models ---
data class ChatRequestCreate(
    @SerializedName("recipient_id") val recipientId: Int,
    @SerializedName("body") val body: String
)

data class MessageCreate(
    @SerializedName("body") val body: String
)

data class MessageResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("chat_id") val chatId: Int,
    @SerializedName("sender_id") val senderId: Int,
    @SerializedName("body") val body: String,
    @SerializedName("created_at") val createdAt: String
)

data class ChatResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("requester_id") val requesterId: Int,
    @SerializedName("recipient_id") val recipientId: Int,
    @SerializedName("status") val status: String,
    @SerializedName("is_permanent") val isPermanent: Boolean,
    @SerializedName("last_message_at") val lastMessageAt: String? = null,
    @SerializedName("created_at") val createdAt: String
)

// --- Content Models ---
data class InviteCreate(
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String? = null,
    @SerializedName("ttl_minutes") val ttlMinutes: Int = 180
)

data class InviteRemoteResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String? = null,
    @SerializedName("expires_at") val expiresAt: String,
    @SerializedName("created_at") val createdAt: String
)

data class StoryCreate(
    @SerializedName("text") val text: String,
    @SerializedName("media_url") val mediaUrl: String? = null,
    @SerializedName("ttl_minutes") val ttlMinutes: Int = 180
)

data class StoryResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("text") val text: String,
    @SerializedName("media_url") val mediaUrl: String? = null,
    @SerializedName("expires_at") val expiresAt: String,
    @SerializedName("created_at") val createdAt: String
)

// --- Safety Models ---
data class BlockCreate(
    @SerializedName("user_id") val userId: Int
)

data class ReportCreate(
    @SerializedName("reported_id") val reportedId: Int,
    @SerializedName("reason") val reason: String,
    @SerializedName("details") val details: String? = null
)

data class TrustedContactCreate(
    @SerializedName("name") val name: String,
    @SerializedName("phone_number") val phoneNumber: String
)

data class SafeMeetCreate(
    @SerializedName("trusted_contact_id") val trustedContactId: Int? = null,
    @SerializedName("chat_id") val chatId: Int? = null,
    @SerializedName("eta_minutes") val etaMinutes: Int
)

data class SafetyCreated(
    @SerializedName("id") val id: Int,
    @SerializedName("created_at") val createdAt: String
)
