package com.example.data.repository

import com.example.data.local.DailyMetricsEntity
import com.example.data.local.WorkoutDao
import com.example.data.local.WorkoutHistoryEntity
import com.example.data.model.WorkoutPlan
import kotlinx.coroutines.flow.Flow

class WorkoutRepository(private val workoutDao: WorkoutDao) {

    fun getWorkoutPlans(): List<WorkoutPlan> = ExercisesData.samplePlans

    fun getWorkoutHistory(): Flow<List<WorkoutHistoryEntity>> = workoutDao.getAllHistory()

    suspend fun recordCompletedWorkout(title: String, durationSeconds: Int, calories: Int, exercisesCount: Int) {
        val entity = WorkoutHistoryEntity(
            workoutTitle = title,
            dateCompleted = System.currentTimeMillis(),
            durationSeconds = durationSeconds,
            caloriesBurned = calories,
            exercisesCompleted = exercisesCount
        )
        workoutDao.insertHistory(entity)
    }

    fun getDailyMetrics(dateIso: String): Flow<DailyMetricsEntity?> = workoutDao.getDailyMetrics(dateIso)

    suspend fun updateDailyMetrics(dateIso: String, steps: Int, waterMl: Int, calories: Int) {
        workoutDao.upsertDailyMetrics(
            DailyMetricsEntity(
                dateIso = dateIso,
                stepsCount = steps,
                waterMl = waterMl,
                caloriesBurned = calories
            )
        )
    }
}
