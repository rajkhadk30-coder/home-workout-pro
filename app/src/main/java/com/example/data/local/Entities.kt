package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_history")
data class WorkoutHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val workoutTitle: String,
    val dateCompleted: Long,
    val durationSeconds: Int,
    val caloriesBurned: Int,
    val exercisesCompleted: Int
)

@Entity(tableName = "daily_metrics")
data class DailyMetricsEntity(
    @PrimaryKey val dateIso: String,
    val stepsCount: Int = 0,
    val waterMl: Int = 0,
    val caloriesBurned: Int = 0
)
