package com.example.nearnow.data.remote.api

import com.example.nearnow.data.remote.model.*
import retrofit2.Response
import retrofit2.http.*

interface NearNowApiService {

    // --- Auth API ---
    @POST("auth/otp/send")
    suspend fun sendOtp(
        @Body request: OtpSendRequest
    ): OtpSendResponse

    @POST("auth/otp/verify")
    suspend fun verifyOtp(
        @Body request: OtpVerifyRequest
    ): TokenResponse

    @POST("auth/firebase/verify")
    suspend fun verifyFirebaseToken(
        @Body request: FirebaseVerifyRequest
    ): TokenResponse

    // --- Profile API ---
    @GET("profile/me")
    suspend fun getMyProfile(): ProfileResponse

    @PUT("profile/me")
    suspend fun updateMyProfile(
        @Body request: ProfileUpsert
    ): ProfileResponse

    // --- Discovery API ---
    @PUT("discovery/location")
    suspend fun updateLocation(
        @Body request: LocationUpdate
    ): Response<Unit>

    @GET("discovery/nearby")
    suspend fun getNearbyUsers(): NearbyResponse

    @PUT("discovery/radius")
    suspend fun updateRadius(
        @Body request: RadiusUpdate
    ): ProfileResponse

    @PUT("discovery/invisible")
    suspend fun updateInvisible(
        @Body request: InvisibleUpdate
    ): ProfileResponse

    // --- Chat API ---
    @POST("chat/request")
    suspend fun requestChat(
        @Body request: ChatRequestCreate
    ): ChatResponse

    @GET("chat/list")
    suspend fun getChats(): List<ChatResponse>

    @GET("chat/{chat_id}/messages")
    suspend fun getMessages(
        @Path("chat_id") chatId: Int
    ): List<MessageResponse>

    @POST("chat/{chat_id}/messages")
    suspend fun sendMessage(
        @Path("chat_id") chatId: Int,
        @Body request: MessageCreate
    ): MessageResponse

    @POST("chat/{chat_id}/permanent")
    suspend fun makeChatPermanent(
        @Path("chat_id") chatId: Int
    ): ChatResponse

    // --- Invites API ---
    @POST("invites")
    suspend fun createInvite(
        @Body request: InviteCreate
    ): InviteRemoteResponse

    @GET("invites/nearby")
    suspend fun getNearbyInvites(): List<InviteRemoteResponse>

    // --- Stories API ---
    @POST("stories")
    suspend fun createStory(
        @Body request: StoryCreate
    ): StoryResponse

    @GET("stories/nearby")
    suspend fun getNearbyStories(): List<StoryResponse>

    // --- Safety API ---
    @POST("safety/block")
    suspend fun blockUser(
        @Body request: BlockCreate
    ): SafetyCreated

    @POST("safety/report")
    suspend fun reportUser(
        @Body request: ReportCreate
    ): SafetyCreated

    @POST("safety/trusted-contact")
    suspend fun addTrustedContact(
        @Body request: TrustedContactCreate
    ): SafetyCreated

    @POST("safety/safe-meet")
    suspend fun triggerSafeMeet(
        @Body request: SafeMeetCreate
    ): SafetyCreated
}
