package com.example.drift.data.remote

import android.app.Activity
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

object FirebaseAuthManager {
    private const val TAG = "FirebaseAuthManager"
    private const val BACKEND_URL = "https://nearnowbackend-production.up.railway.app/auth/firebase/verify"
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    private var firebaseAuth: FirebaseAuth? = null

    init {
        try {
            firebaseAuth = FirebaseAuth.getInstance()
            // Support Firebase Auth Emulator if configured
            // e.g. firebaseAuth?.useEmulator("10.0.2.2", 9099)
        } catch (e: Exception) {
            Log.w(TAG, "Firebase not initialized. Falling back to mock auth.", e)
        }
    }

    /**
     * Sends OTP to the specified phone number.
     * If the phone number is a mock number, triggers local mock bypass immediately.
     */
    fun sendOtp(
        phoneNumber: String,
        activity: Activity,
        onCodeSent: (verificationId: String) -> Unit,
        onVerificationFailed: (Exception) -> Unit
    ) {
        Log.d(TAG, "sendOtp to: $phoneNumber")
        
        // Mock phone numbers logic (e.g. +1 555-* or local mock numbers)
        if (isMockPhoneNumber(phoneNumber) || firebaseAuth == null) {
            Log.d(TAG, "Using Mock Auth for phone number: $phoneNumber")
            // Instantly trigger mock code sent with a mock verification ID
            onCodeSent("mock-verification-id-${phoneNumber}")
            return
        }

        val options = PhoneAuthOptions.newBuilder(firebaseAuth!!)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    Log.d(TAG, "onVerificationCompleted: $credential")
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.w(TAG, "onVerificationFailed", e)
                    onVerificationFailed(e)
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    Log.d(TAG, "onCodeSent: $verificationId")
                    onCodeSent(verificationId)
                }
            })
            .build()
        
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    /**
     * Verifies the OTP code.
     * In mock mode, returns a mock JWT from the backend.
     * In production, logs in via Firebase Auth, gets ID Token, and verifies with backend.
     */
    fun verifyOtpAndAuthenticate(
        verificationId: String,
        code: String,
        onSuccess: (token: String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        Log.d(TAG, "verifyOtpAndAuthenticate verificationId: $verificationId")

        if (verificationId.startsWith("mock-verification-id-")) {
            val phone = verificationId.removePrefix("mock-verification-id-")
            val mockToken = "mock-firebase-token-$phone"
            verifyTokenWithBackend(mockToken, onSuccess, onFailure)
            return
        }

        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        val auth = firebaseAuth
        if (auth == null) {
            onFailure(IllegalStateException("Firebase Auth is not available"))
            return
        }

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    user?.getIdToken(true)
                        ?.addOnCompleteListener { tokenTask ->
                            if (tokenTask.isSuccessful) {
                                val idToken = tokenTask.result?.token
                                if (idToken != null) {
                                    verifyTokenWithBackend(idToken, onSuccess, onFailure)
                                } else {
                                    onFailure(Exception("ID Token was null"))
                                }
                            } else {
                                onFailure(tokenTask.exception ?: Exception("Failed to get ID Token"))
                            }
                        }
                } else {
                    onFailure(task.exception ?: Exception("Firebase authentication failed"))
                }
            }
    }

    private fun verifyTokenWithBackend(
        idToken: String,
        onSuccess: (token: String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val json = JSONObject().apply {
            put("id_token", idToken)
        }
        val requestBody = json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        
        val request = Request.Builder()
            .url(BACKEND_URL)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Backend token verification failed", e)
                onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        Log.e(TAG, "Backend token verification failed with code: ${response.code}")
                        onFailure(IOException("Unexpected response code: ${response.code}"))
                        return
                    }

                    val bodyString = response.body?.string() ?: ""
                    try {
                        val jsonResponse = JSONObject(bodyString)
                        val accessToken = jsonResponse.getString("access_token")
                        onSuccess(accessToken)
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to parse token response", e)
                        onFailure(e)
                    }
                }
            }
        })
    }

    private fun isMockPhoneNumber(phoneNumber: String): Boolean {
        // Any phone number like "+1 555-..." or standard test numbers are considered mocks
        return phoneNumber.contains("555") || phoneNumber == "+919876500001" || phoneNumber == "9876500001"
    }
}
