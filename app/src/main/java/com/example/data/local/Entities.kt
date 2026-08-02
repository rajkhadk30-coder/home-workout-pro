package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "Athlete",
    val email: String = "guest@homeworkout.pro",
    val authType: String = "GUEST",
    val heightCm: Float = 172f,
    val weightKg: Float = 68f,
    val targetWeightKg: Float = 65f,
    val age: Int = 24,
    val gender: String = "Male",
    val language: String = "ENGLISH",
    val isDarkMode: Boolean = true,
    val currentStreak: Int = 3,
    val xp: Int = 450,
    val level: Int = 3,
    val lastActiveTimestamp: Long = System.currentTimeMillis(),
    val waterGoalMl: Int = 2500,
    val stepGoal: Int = 10000,
    val dailyRemindersEnabled: Boolean = true
)

@Entity(tableName = "workout_logs")
data class WorkoutLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val workoutTitle: String,
    val category: String,
    val durationSeconds: Int,
    val caloriesBurned: Int,
    val exercisesCount: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val xpEarned: Int = 100
)

@Entity(tableName = "water_logs")
data class WaterLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amountMl: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val dateString: String
)

@Entity(tableName = "weight_logs")
data class WeightLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val weightKg: Float,
    val timestamp: Long = System.currentTimeMillis(),
    val dateString: String
)

@Entity(tableName = "body_measurements")
data class BodyMeasurementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val chestCm: Float,
    val waistCm: Float,
    val bicepsCm: Float,
    val thighsCm: Float,
    val hipsCm: Float,
    val timestamp: Long = System.currentTimeMillis(),
    val dateString: String
)

@Entity(tableName = "custom_exercises")
data class CustomExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nameEn: String,
    val nameNp: String,
    val category: String,
    val targetMuscle: String,
    val difficulty: String,
    val caloriesBurned: Int,
    val durationSeconds: Int,
    val repetitions: Int,
    val videoUrl: String
)
