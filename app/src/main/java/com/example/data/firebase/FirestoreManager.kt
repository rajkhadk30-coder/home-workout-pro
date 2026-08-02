package com.example.data.firebase

import android.util.Log
import com.example.data.local.UserProfileEntity
import com.example.data.local.WorkoutLogEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class FirestoreManager {
    private val TAG = "FirestoreManager"

    private val db: FirebaseFirestore?
        get() = try {
            FirebaseFirestore.getInstance()
        } catch (e: Throwable) {
            Log.w(TAG, "Firestore not available: ${e.message}")
            null
        }

    suspend fun saveUserProfileToFirestore(userId: String, profile: UserProfileEntity) {
        try {
            val firestore = db ?: return
            val userMap = mapOf(
                "name" to profile.name,
                "email" to profile.email,
                "authType" to profile.authType,
                "gender" to profile.gender,
                "age" to profile.age,
                "heightCm" to profile.heightCm,
                "weightKg" to profile.weightKg,
                "targetWeightKg" to profile.targetWeightKg,
                "xp" to profile.xp,
                "level" to profile.level,
                "currentStreak" to profile.currentStreak,
                "language" to profile.language,
                "isDarkMode" to profile.isDarkMode,
                "updatedAt" to System.currentTimeMillis()
            )
            firestore.collection("users").document(userId)
                .set(userMap, SetOptions.merge())
                .await()
            Log.d(TAG, "User profile successfully saved to Firestore for $userId")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save profile to Firestore: ${e.message}")
        }
    }

    suspend fun recordWorkoutToFirestore(userId: String, log: WorkoutLogEntity) {
        try {
            val firestore = db ?: return
            val logMap = mapOf(
                "workoutTitle" to log.workoutTitle,
                "category" to log.category,
                "durationSeconds" to log.durationSeconds,
                "caloriesBurned" to log.caloriesBurned,
                "exercisesCount" to log.exercisesCount,
                "xpEarned" to log.xpEarned,
                "timestamp" to log.timestamp
            )
            firestore.collection("users")
                .document(userId)
                .collection("workout_logs")
                .add(logMap)
                .await()
            Log.d(TAG, "Workout log saved to Firestore")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save workout log to Firestore: ${e.message}")
        }
    }

    suspend fun recordWaterToFirestore(userId: String, amountMl: Int, dateString: String) {
        try {
            val firestore = db ?: return
            val waterMap = mapOf(
                "amountMl" to amountMl,
                "dateString" to dateString,
                "timestamp" to System.currentTimeMillis()
            )
            firestore.collection("users")
                .document(userId)
                .collection("water_logs")
                .add(waterMap)
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save water log to Firestore: ${e.message}")
        }
    }

    suspend fun fetchUserProfileFromFirestore(userId: String): UserProfileEntity? {
        return try {
            val firestore = db ?: return null
            val doc = firestore.collection("users").document(userId).get().await()
            if (doc.exists()) {
                UserProfileEntity(
                    name = doc.getString("name") ?: "Athlete",
                    email = doc.getString("email") ?: "",
                    authType = doc.getString("authType") ?: "FIREBASE",
                    gender = doc.getString("gender") ?: "Male",
                    age = (doc.getLong("age") ?: 25).toInt(),
                    heightCm = (doc.getDouble("heightCm") ?: 175.0).toFloat(),
                    weightKg = (doc.getDouble("weightKg") ?: 70.0).toFloat(),
                    targetWeightKg = (doc.getDouble("targetWeightKg") ?: 65.0).toFloat(),
                    xp = (doc.getLong("xp") ?: 0).toInt(),
                    level = (doc.getLong("level") ?: 1).toInt(),
                    currentStreak = (doc.getLong("currentStreak") ?: 1).toInt(),
                    language = doc.getString("language") ?: "ENGLISH",
                    isDarkMode = doc.getBoolean("isDarkMode") ?: true
                )
            } else null
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch user profile from Firestore: ${e.message}")
            null
        }
    }
}
