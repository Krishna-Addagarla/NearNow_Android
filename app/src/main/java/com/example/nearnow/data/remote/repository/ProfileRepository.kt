package com.example.nearnow.data.remote.repository

import com.example.nearnow.data.remote.api.NearNowApiService
import com.example.nearnow.data.remote.model.ProfileResponse
import com.example.nearnow.data.remote.model.ProfileUpsert
import javax.inject.Inject
import javax.inject.Singleton

interface ProfileRepository {
    suspend fun getMyProfile(): Result<ProfileResponse>
    suspend fun updateMyProfile(profile: ProfileUpsert): Result<ProfileResponse>
}

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val apiService: NearNowApiService
) : ProfileRepository {

    override suspend fun getMyProfile(): Result<ProfileResponse> = runCatching {
        apiService.getMyProfile()
    }

    override suspend fun updateMyProfile(profile: ProfileUpsert): Result<ProfileResponse> = runCatching {
        apiService.updateMyProfile(profile)
    }
}
