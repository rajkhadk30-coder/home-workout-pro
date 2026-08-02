package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.DailyMetricsEntity
import com.example.data.local.WorkoutHistoryEntity
import com.example.data.model.UserProfile
import com.example.data.model.WorkoutPlan
import com.example.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WorkoutRepository
    val todayIso: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private val _workoutPlans = MutableStateFlow<List<WorkoutPlan>>(emptyList())
    val workoutPlans: StateFlow<List<WorkoutPlan>> = _workoutPlans.asStateFlow()

    private val _selectedPlan = MutableStateFlow<WorkoutPlan?>(null)
    val selectedPlan: StateFlow<WorkoutPlan?> = _selectedPlan.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _dailyWaterMl = MutableStateFlow(1250)
    val dailyWaterMl: StateFlow<Int> = _dailyWaterMl.asStateFlow()

    private val _dailySteps = MutableStateFlow(4250)
    val dailySteps: StateFlow<Int> = _dailySteps.asStateFlow()

    private val _workoutHistory = MutableStateFlow<List<WorkoutHistoryEntity>>(emptyList())
    val workoutHistory: StateFlow<List<WorkoutHistoryEntity>> = _workoutHistory.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = WorkoutRepository(database.workoutDao())
        _workoutPlans.value = repository.getWorkoutPlans()

        viewModelScope.launch {
            repository.getWorkoutHistory().collect { historyList ->
                _workoutHistory.value = historyList
            }
        }
    }

    fun selectWorkoutPlan(plan: WorkoutPlan) {
        _selectedPlan.value = plan
    }

    fun addWater(amountMl: Int) {
        _dailyWaterMl.value = (_dailyWaterMl.value + amountMl).coerceAtLeast(0)
    }

    fun addSteps(steps: Int) {
        _dailySteps.value = (_dailySteps.value + steps).coerceAtLeast(0)
    }

    fun finishWorkout(title: String, durationSeconds: Int, calories: Int, exerciseCount: Int) {
        viewModelScope.launch {
            repository.recordCompletedWorkout(
                title = title,
                durationSeconds = durationSeconds,
                calories = calories,
                exercisesCount = exerciseCount
            )
        }
    }

    fun updateUserProfile(profile: UserProfile) {
        _userProfile.value = profile
    }
}
