package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.firebase.AuthUser
import com.example.data.firebase.FirebaseAuthManager
import com.example.data.firebase.FirestoreManager
import com.example.data.gemini.ChatMessage
import com.example.data.gemini.ChatSender
import com.example.data.gemini.GeminiChatService
import com.example.data.gemini.GeminiModel
import com.example.data.local.AppDatabase
import com.example.data.local.UserProfileEntity
import com.example.data.local.WorkoutLogEntity
import com.example.data.model.*
import com.example.data.repository.WorkoutRepository
import com.example.util.StepCounterSensor
import com.example.util.TtsHelper
import com.example.util.WorkoutNotificationManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repository = WorkoutRepository(db.workoutDao())
    val stepSensor = StepCounterSensor(application)
    val ttsHelper = TtsHelper(application)
    val notificationManager = WorkoutNotificationManager(application)

    // Firebase Auth & Firestore Managers
    val authManager = FirebaseAuthManager()
    val firestoreManager = FirestoreManager()
    val geminiChatService = GeminiChatService()

    val authUser: StateFlow<AuthUser?> = authManager.currentUser
    val authError = MutableStateFlow<String?>(null)
    val isAuthLoading = MutableStateFlow<Boolean>(false)

    // Gemini Chatbot State
    val chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val isChatLoading = MutableStateFlow<Boolean>(false)
    val selectedGeminiModel = MutableStateFlow<GeminiModel>(GeminiModel.GENERAL)

    // User Profile & Preferences
    val userProfile: StateFlow<UserProfileEntity> = repository.userProfile
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfileEntity()
        )

    val currentLanguage: StateFlow<Language> = userProfile
        .map {
            try {
                Language.valueOf(it.language)
            } catch (e: Exception) {
                Language.ENGLISH
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Language.ENGLISH
        )

    val isDarkMode: StateFlow<Boolean> = userProfile
        .map { it.isDarkMode }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    // Workout Data & Logs
    val workoutLogs = repository.workoutLogs
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val waterLogs = repository.getWaterLogsForToday()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val weightLogs = repository.weightLogs
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val bodyMeasurements = repository.bodyMeasurements
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val customExercises = repository.customExercises
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val stepsCount = stepSensor.steps

    init {
        stepSensor.startListening()
        // Welcome message in Gemini Chat
        chatMessages.value = listOf(
            ChatMessage(
                sender = ChatSender.BOT,
                text = "Welcome to Home Workout Pro AI Coach! I am powered by Gemini. Ask me for customized workout plans, macro targets, exercise form tips, or diet recommendations.",
                modelUsed = selectedGeminiModel.value.displayName
            )
        )
    }

    // --- Firebase Auth Actions ---

    fun loginWithEmail(email: String, pass: String, name: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isAuthLoading.value = true
            authError.value = null
            val result = authManager.signInWithEmail(email, pass)
            isAuthLoading.value = false
            result.fold(
                onSuccess = { user ->
                    updateUserProfile(user.displayName ?: name, user.email ?: email, userProfile.value.heightCm, userProfile.value.weightKg, userProfile.value.targetWeightKg, "EMAIL")
                    syncProfileToFirestore(user.uid)
                    onSuccess()
                },
                onFailure = { err ->
                    authError.value = err.localizedMessage ?: "Login failed"
                }
            )
        }
    }

    fun signUpWithEmail(email: String, pass: String, name: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isAuthLoading.value = true
            authError.value = null
            val result = authManager.signUpWithEmail(email, pass, name)
            isAuthLoading.value = false
            result.fold(
                onSuccess = { user ->
                    updateUserProfile(name, email, userProfile.value.heightCm, userProfile.value.weightKg, userProfile.value.targetWeightKg, "EMAIL")
                    syncProfileToFirestore(user.uid)
                    onSuccess()
                },
                onFailure = { err ->
                    authError.value = err.localizedMessage ?: "Sign up failed"
                }
            )
        }
    }

    fun loginAnonymously(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isAuthLoading.value = true
            val result = authManager.signInAnonymously()
            isAuthLoading.value = false
            result.getOrNull()?.let { user ->
                updateUserProfile("Guest Athlete", "guest@homeworkout.pro", userProfile.value.heightCm, userProfile.value.weightKg, userProfile.value.targetWeightKg, "GUEST")
                syncProfileToFirestore(user.uid)
            }
            onSuccess()
        }
    }

    fun sendPasswordReset(email: String, onComplete: (String) -> Unit) {
        viewModelScope.launch {
            val result = authManager.sendPasswordReset(email)
            result.fold(
                onSuccess = { onComplete("Password reset link sent to $email!") },
                onFailure = { err -> onComplete("Error: ${err.localizedMessage}") }
            )
        }
    }

    fun signOutUser() {
        authManager.signOut()
    }

    private fun syncProfileToFirestore(userId: String) {
        viewModelScope.launch {
            firestoreManager.saveUserProfileToFirestore(userId, userProfile.value)
        }
    }

    // --- Gemini Chat Actions ---

    fun selectGeminiModel(model: GeminiModel) {
        selectedGeminiModel.value = model
    }

    fun sendMessageToGemini(userPrompt: String) {
        val userMsg = ChatMessage(
            sender = ChatSender.USER,
            text = userPrompt
        )
        val currentList = chatMessages.value.toMutableList()
        currentList.add(userMsg)
        chatMessages.value = currentList
        isChatLoading.value = true

        viewModelScope.launch {
            val responseMsg = geminiChatService.sendMessage(
                history = currentList,
                userPrompt = userPrompt,
                selectedModel = selectedGeminiModel.value
            )
            val updatedList = chatMessages.value.toMutableList()
            updatedList.add(responseMsg)
            chatMessages.value = updatedList
            isChatLoading.value = false
        }
    }

    // --- Core Workout Actions ---

    fun setLanguage(language: Language) {
        viewModelScope.launch {
            repository.updateLanguage(language)
        }
    }

    fun toggleThemeMode() {
        viewModelScope.launch {
            val current = isDarkMode.value
            repository.updateThemeMode(!current)
        }
    }

    fun updateUserProfile(name: String, email: String, heightCm: Float, weightKg: Float, targetWeightKg: Float, authType: String = "GUEST") {
        viewModelScope.launch {
            val current = userProfile.value
            val updated = current.copy(
                name = name,
                email = email,
                authType = authType,
                heightCm = heightCm,
                weightKg = weightKg,
                targetWeightKg = targetWeightKg
            )
            repository.saveProfile(updated)

            authUser.value?.uid?.let { uid ->
                firestoreManager.saveUserProfileToFirestore(uid, updated)
            }
        }
    }

    fun recordCompletedWorkout(title: String, category: String, durationSec: Int, calories: Int, exerciseCount: Int) {
        viewModelScope.launch {
            repository.recordWorkout(title, category, durationSec, calories, exerciseCount)
            notificationManager.sendNotification(
                "Workout Complete! 🔥",
                "Awesome job completing $title! You earned +100 XP."
            )

            authUser.value?.uid?.let { uid ->
                firestoreManager.recordWorkoutToFirestore(
                    uid,
                    WorkoutLogEntity(
                        workoutTitle = title,
                        category = category,
                        durationSeconds = durationSec,
                        caloriesBurned = calories,
                        exercisesCount = exerciseCount,
                        xpEarned = 100 + (calories / 2)
                    )
                )
            }
        }
    }

    fun addWater(amountMl: Int) {
        viewModelScope.launch {
            repository.addWaterIntake(amountMl)
        }
    }

    fun resetWater() {
        viewModelScope.launch {
            repository.resetWaterIntake()
        }
    }

    fun logWeight(weightKg: Float) {
        viewModelScope.launch {
            repository.logWeight(weightKg)
        }
    }

    fun logBodyMeasurement(chest: Float, waist: Float, biceps: Float, thighs: Float, hips: Float) {
        viewModelScope.launch {
            repository.logBodyMeasurement(chest, waist, biceps, thighs, hips)
        }
    }

    fun addCustomExercise(
        nameEn: String,
        nameNp: String,
        category: String,
        targetMuscle: String,
        difficulty: String,
        calories: Int,
        duration: Int,
        reps: Int,
        videoUrl: String
    ) {
        viewModelScope.launch {
            repository.addCustomExercise(
                nameEn, nameNp, category, targetMuscle, difficulty, calories, duration, reps, videoUrl
            )
        }
    }

    fun deleteCustomExercise(id: Long) {
        viewModelScope.launch {
            repository.deleteCustomExercise(id)
        }
    }

    fun speakVoiceCoach(text: String) {
        ttsHelper.speak(text, currentLanguage.value)
    }

    fun simulateWalkSteps() {
        stepSensor.simulateWalkSteps(500)
    }

    fun sendAdminNotification(title: String, message: String) {
        notificationManager.sendNotification(title, message)
    }

    override fun onCleared() {
        super.onCleared()
        stepSensor.stopListening()
        ttsHelper.shutdown()
    }
}
