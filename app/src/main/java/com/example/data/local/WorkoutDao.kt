package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workout_history ORDER BY dateCompleted DESC")
    fun getAllHistory(): Flow<List<WorkoutHistoryEntity>>

    @Insert
    suspend fun insertHistory(history: WorkoutHistoryEntity)

    @Query("SELECT * FROM daily_metrics WHERE dateIso = :dateIso LIMIT 1")
    fun getDailyMetrics(dateIso: String): Flow<DailyMetricsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDailyMetrics(metrics: DailyMetricsEntity)
}
