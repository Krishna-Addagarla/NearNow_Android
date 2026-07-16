package com.example.nearnow.data.remote.repository

import com.example.nearnow.data.remote.api.NearNowApiService
import com.example.nearnow.data.remote.model.LocationUpdate
import com.example.nearnow.data.remote.model.NearbyResponse
import com.example.nearnow.data.remote.model.ProfileResponse
import com.example.nearnow.data.remote.model.RadiusUpdate
import com.example.nearnow.data.remote.model.InvisibleUpdate
import javax.inject.Inject
import javax.inject.Singleton

interface DiscoveryRepository {
    suspend fun updateLocation(latitude: Double, longitude: Double): Result<Unit>
    suspend fun getNearbyUsers(): Result<NearbyResponse>
    suspend fun updateRadius(radiusM: Int): Result<ProfileResponse>
    suspend fun updateInvisible(isInvisible: Boolean): Result<ProfileResponse>
}

@Singleton
class DiscoveryRepositoryImpl @Inject constructor(
    private val apiService: NearNowApiService
) : DiscoveryRepository {

    override suspend fun updateLocation(latitude: Double, longitude: Double): Result<Unit> = runCatching {
        val response = apiService.updateLocation(LocationUpdate(latitude, longitude))
        if (!response.isSuccessful) {
            throw Exception("Failed to update location, code: ${response.code()}")
        }
    }

    override suspend fun getNearbyUsers(): Result<NearbyResponse> = runCatching {
        apiService.getNearbyUsers()
    }

    override suspend fun updateRadius(radiusM: Int): Result<ProfileResponse> = runCatching {
        apiService.updateRadius(RadiusUpdate(radiusM))
    }

    override suspend fun updateInvisible(isInvisible: Boolean): Result<ProfileResponse> = runCatching {
        apiService.updateInvisible(InvisibleUpdate(isInvisible))
    }
}
