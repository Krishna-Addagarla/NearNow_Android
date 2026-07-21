package com.example.nearnow

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.nearnow.data.local.SessionManager
import com.example.nearnow.data.remote.repository.DiscoveryRepository
import com.example.nearnow.data.remote.repository.ProfileRepository
import com.example.nearnow.ui.navigation.NavHost
import com.example.nearnow.ui.navigation.rememberNavController
import com.example.nearnow.ui.theme.NearNowTheme
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ServiceEntryPoint {
    fun profileRepository(): ProfileRepository
    fun discoveryRepository(): DiscoveryRepository
}

class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val sessionManager = SessionManager(applicationContext)
        val entryPoint = EntryPoints.get(applicationContext, ServiceEntryPoint::class.java)
        val profileRepository = entryPoint.profileRepository()
        val discoveryRepository = entryPoint.discoveryRepository()

        setContent {
            NearNowTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        sessionManager = sessionManager,
                        profileRepository = profileRepository,
                        discoveryRepository = discoveryRepository,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}