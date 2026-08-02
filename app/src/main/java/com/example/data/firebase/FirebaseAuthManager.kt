package com.example.data.firebase

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

data class AuthUser(
    val uid: String,
    val displayName: String?,
    val email: String?,
    val isAnonymous: Boolean,
    val providerId: String
)

class FirebaseAuthManager {
    private val TAG = "FirebaseAuthManager"
    
    private val auth: FirebaseAuth?
        get() = try {
            FirebaseAuth.getInstance()
        } catch (e: Throwable) {
            Log.w(TAG, "Firebase Auth not available: ${e.message}")
            null
        }

    private val _currentUser = MutableStateFlow<AuthUser?>(getCurrentAuthUser())
    val currentUser: StateFlow<AuthUser?> = _currentUser

    fun getCurrentAuthUser(): AuthUser? {
        val fbUser = auth?.currentUser ?: return null
        return AuthUser(
            uid = fbUser.uid,
            displayName = fbUser.displayName,
            email = fbUser.email,
            isAnonymous = fbUser.isAnonymous,
            providerId = fbUser.providerData.firstOrNull()?.providerId ?: "firebase"
        )
    }

    suspend fun signInWithEmail(email: String, pass: String): Result<AuthUser> {
        return try {
            val a = auth ?: return Result.failure(Exception("Firebase Auth unavailable"))
            val res = a.signInWithEmailAndPassword(email, pass).await()
            val user = res.user
            if (user != null) {
                val authUser = AuthUser(
                    uid = user.uid,
                    displayName = user.displayName ?: email.substringBefore("@"),
                    email = user.email ?: email,
                    isAnonymous = false,
                    providerId = "PASSWORD"
                )
                _currentUser.value = authUser
                Result.success(authUser)
            } else {
                Result.failure(Exception("Login failed: empty user"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Email login error: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun signUpWithEmail(email: String, pass: String, name: String): Result<AuthUser> {
        return try {
            val a = auth ?: return Result.failure(Exception("Firebase Auth unavailable"))
            val res = a.createUserWithEmailAndPassword(email, pass).await()
            val user = res.user
            if (user != null) {
                val profileUpdates = com.google.firebase.auth.userProfileChangeRequest {
                    displayName = name
                }
                user.updateProfile(profileUpdates).await()

                val authUser = AuthUser(
                    uid = user.uid,
                    displayName = name,
                    email = user.email ?: email,
                    isAnonymous = false,
                    providerId = "PASSWORD"
                )
                _currentUser.value = authUser
                Result.success(authUser)
            } else {
                Result.failure(Exception("Sign up failed"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Sign up error: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun signInAnonymously(): Result<AuthUser> {
        return try {
            val a = auth ?: return Result.success(
                AuthUser("guest_" + System.currentTimeMillis(), "Guest Athlete", "guest@homeworkout.pro", true, "GUEST")
            )
            val res = a.signInAnonymously().await()
            val user = res.user
            val authUser = AuthUser(
                uid = user?.uid ?: ("guest_" + System.currentTimeMillis()),
                displayName = "Guest Athlete",
                email = "guest@homeworkout.pro",
                isAnonymous = true,
                providerId = "GUEST"
            )
            _currentUser.value = authUser
            Result.success(authUser)
        } catch (e: Exception) {
            Log.w(TAG, "Anonymous auth failed, fallback to local guest: ${e.message}")
            val fallback = AuthUser("guest_" + System.currentTimeMillis(), "Guest Athlete", "guest@homeworkout.pro", true, "GUEST")
            _currentUser.value = fallback
            Result.success(fallback)
        }
    }

    suspend fun sendPasswordReset(email: String): Result<Unit> {
        return try {
            val a = auth ?: return Result.failure(Exception("Firebase Auth unavailable"))
            a.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        try {
            auth?.signOut()
        } catch (e: Exception) {
            Log.e(TAG, "Sign out error: ${e.message}")
        }
        _currentUser.value = null
    }
}
