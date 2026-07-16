package com.example.nearnow.data.remote.repository

import com.example.nearnow.data.remote.api.NearNowApiService
import com.example.nearnow.data.remote.model.InviteCreate
import com.example.nearnow.data.remote.model.InviteRemoteResponse
import com.example.nearnow.data.remote.model.StoryCreate
import com.example.nearnow.data.remote.model.StoryResponse
import javax.inject.Inject
import javax.inject.Singleton

interface ContentRepository {
    suspend fun createInvite(title: String, body: String?, ttlMinutes: Int): Result<InviteRemoteResponse>
    suspend fun getNearbyInvites(): Result<List<InviteRemoteResponse>>
    suspend fun createStory(text: String, mediaUrl: String?, ttlMinutes: Int): Result<StoryResponse>
    suspend fun getNearbyStories(): Result<List<StoryResponse>>
}

@Singleton
class ContentRepositoryImpl @Inject constructor(
    private val apiService: NearNowApiService
) : ContentRepository {

    override suspend fun createInvite(title: String, body: String?, ttlMinutes: Int): Result<InviteRemoteResponse> = runCatching {
        apiService.createInvite(InviteCreate(title, body, ttlMinutes))
    }

    override suspend fun getNearbyInvites(): Result<List<InviteRemoteResponse>> = runCatching {
        apiService.getNearbyInvites()
    }

    override suspend fun createStory(text: String, mediaUrl: String?, ttlMinutes: Int): Result<StoryResponse> = runCatching {
        apiService.createStory(StoryCreate(text, mediaUrl, ttlMinutes))
    }

    override suspend fun getNearbyStories(): Result<List<StoryResponse>> = runCatching {
        apiService.getNearbyStories()
    }
}
