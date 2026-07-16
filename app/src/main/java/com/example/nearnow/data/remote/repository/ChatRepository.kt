package com.example.nearnow.data.remote.repository

import com.example.nearnow.data.remote.api.NearNowApiService
import com.example.nearnow.data.remote.model.ChatRequestCreate
import com.example.nearnow.data.remote.model.ChatResponse
import com.example.nearnow.data.remote.model.MessageCreate
import com.example.nearnow.data.remote.model.MessageResponse
import javax.inject.Inject
import javax.inject.Singleton

interface ChatRepository {
    suspend fun requestChat(recipientId: Int, body: String): Result<ChatResponse>
    suspend fun getChats(): Result<List<ChatResponse>>
    suspend fun getMessages(chatId: Int): Result<List<MessageResponse>>
    suspend fun sendMessage(chatId: Int, body: String): Result<MessageResponse>
    suspend fun makeChatPermanent(chatId: Int): Result<ChatResponse>
}

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val apiService: NearNowApiService
) : ChatRepository {

    override suspend fun requestChat(recipientId: Int, body: String): Result<ChatResponse> = runCatching {
        apiService.requestChat(ChatRequestCreate(recipientId, body))
    }

    override suspend fun getChats(): Result<List<ChatResponse>> = runCatching {
        apiService.getChats()
    }

    override suspend fun getMessages(chatId: Int): Result<List<MessageResponse>> = runCatching {
        apiService.getMessages(chatId)
    }

    override suspend fun sendMessage(chatId: Int, body: String): Result<MessageResponse> = runCatching {
        apiService.sendMessage(chatId, MessageCreate(body))
    }

    override suspend fun makeChatPermanent(chatId: Int): Result<ChatResponse> = runCatching {
        apiService.makeChatPermanent(chatId)
    }
}
