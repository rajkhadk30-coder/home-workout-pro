package com.example.data.model

data class Exercise(
    val id: String,
    val name: String,
    val category: String,
    val durationSeconds: Int = 30,
    val reps: Int = 12,
    val caloriesBurned: Int = 15,
    val description: String,
    val animationType: String = "pushup"
)

data class WorkoutPlan(
    val id: String,
    val title: String,
    val category: String,
    val level: String,
    val durationMinutes: Int,
    val totalCalories: Int,
    val exerciseCount: Int,
    val exercises: List<Exercise>
)

data class UserProfile(
    val name: String = "Athlete",
    val heightCm: Float = 175f,
    val weightKg: Float = 70f,
    val targetWeightKg: Float = 68f,
    val dailyStepGoal: Int = 8000,
    val dailyWaterGoalMl: Int = 2500
)
