package com.example.nearnow.data.remote.repository

import com.example.nearnow.data.remote.api.NearNowApiService
import com.example.nearnow.data.remote.model.*
import javax.inject.Inject
import javax.inject.Singleton

interface AuthRepository {
    suspend fun sendOtp(phoneNumber: String): Result<OtpSendResponse>
    suspend fun verifyOtp(phoneNumber: String, otp: String): Result<TokenResponse>
    suspend fun verifyFirebaseToken(idToken: String): Result<TokenResponse>
}

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: NearNowApiService
) : AuthRepository {

    override suspend fun sendOtp(phoneNumber: String): Result<OtpSendResponse> = runCatching {
        apiService.sendOtp(OtpSendRequest(phoneNumber))
    }

    override suspend fun verifyOtp(phoneNumber: String, otp: String): Result<TokenResponse> = runCatching {
        apiService.verifyOtp(OtpVerifyRequest(phoneNumber, otp))
    }

    override suspend fun verifyFirebaseToken(idToken: String): Result<TokenResponse> = runCatching {
        apiService.verifyFirebaseToken(FirebaseVerifyRequest(idToken))
    }
}
