package com.example.nearnow.data.remote.repository

import com.example.nearnow.data.remote.api.NearNowApiService
import com.example.nearnow.data.remote.model.*
import javax.inject.Inject
import javax.inject.Singleton

interface SafetyRepository {
    suspend fun blockUser(userId: Int): Result<SafetyCreated>
    suspend fun reportUser(reportedId: Int, reason: String, details: String?): Result<SafetyCreated>
    suspend fun addTrustedContact(name: String, phoneNumber: String): Result<SafetyCreated>
    suspend fun triggerSafeMeet(trustedContactId: Int?, chatId: Int?, etaMinutes: Int): Result<SafetyCreated>
}

@Singleton
class SafetyRepositoryImpl @Inject constructor(
    private val apiService: NearNowApiService
) : SafetyRepository {

    override suspend fun blockUser(userId: Int): Result<SafetyCreated> = runCatching {
        apiService.blockUser(BlockCreate(userId))
    }

    override suspend fun reportUser(reportedId: Int, reason: String, details: String?): Result<SafetyCreated> = runCatching {
        apiService.reportUser(ReportCreate(reportedId, reason, details))
    }

    override suspend fun addTrustedContact(name: String, phoneNumber: String): Result<SafetyCreated> = runCatching {
        apiService.addTrustedContact(TrustedContactCreate(name, phoneNumber))
    }

    override suspend fun triggerSafeMeet(trustedContactId: Int?, chatId: Int?, etaMinutes: Int): Result<SafetyCreated> = runCatching {
        apiService.triggerSafeMeet(SafeMeetCreate(trustedContactId, chatId, etaMinutes))
    }
}
