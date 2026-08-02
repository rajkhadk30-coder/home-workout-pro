package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.Exercise
import com.example.data.model.ExerciseCategory
import com.example.data.model.L10n
import com.example.data.model.Language
import com.example.data.repository.ExercisesData
import com.example.ui.MainViewModel
import com.example.ui.screens.*
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.WorkoutProTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            com.google.firebase.FirebaseApp.initializeApp(this)
        } catch (e: Exception) {
            android.util.Log.e("MainActivity", "FirebaseApp init failed: ${e.message}")
        }
        enableEdgeToEdge()

        setContent {
            val mainViewModel: MainViewModel = viewModel()
            val userProfile by mainViewModel.userProfile.collectAsStateWithLifecycle()
            val language by mainViewModel.currentLanguage.collectAsStateWithLifecycle()
            val isDarkMode by mainViewModel.isDarkMode.collectAsStateWithLifecycle()

            val workoutLogs by mainViewModel.workoutLogs.collectAsStateWithLifecycle()
            val waterLogs by mainViewModel.waterLogs.collectAsStateWithLifecycle()
            val weightLogs by mainViewModel.weightLogs.collectAsStateWithLifecycle()
            val bodyMeasurements by mainViewModel.bodyMeasurements.collectAsStateWithLifecycle()
            val customExercises by mainViewModel.customExercises.collectAsStateWithLifecycle()
            val stepsCount by mainViewModel.stepsCount.collectAsStateWithLifecycle()

            val isAuthLoading by mainViewModel.isAuthLoading.collectAsStateWithLifecycle()
            val authError by mainViewModel.authError.collectAsStateWithLifecycle()

            val chatMessages by mainViewModel.chatMessages.collectAsStateWithLifecycle()
            val isChatLoading by mainViewModel.isChatLoading.collectAsStateWithLifecycle()
            val selectedGeminiModel by mainViewModel.selectedGeminiModel.collectAsStateWithLifecycle()

            // Navigation state
            var currentRoute by remember { mutableStateOf("SPLASH") } // SPLASH, ONBOARDING, LOGIN, MAIN, ACTIVE_SESSION, COMPLETE, ADMIN, CHATBOT
            var mainTab by remember { mutableStateOf(0) } // 0: Home, 1: Workouts, 2: Trackers, 3: Diet, 4: Profile

            // Active workout session payload
            var activeExercises by remember { mutableStateOf<List<Exercise>>(emptyList()) }
            var activeSessionTitle by remember { mutableStateOf("Daily Workout") }

            // Last workout completion details
            var lastDuration by remember { mutableStateOf(0) }
            var lastCalories by remember { mutableStateOf(0) }
            var lastExerciseCount by remember { mutableStateOf(0) }

            // Initial Category selection pass-through
            var selectedCategoryFilter by remember { mutableStateOf<ExerciseCategory?>(null) }

            WorkoutProTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (currentRoute) {
                        "SPLASH" -> {
                            SplashScreen(
                                language = language,
                                onNavigateNext = { currentRoute = "ONBOARDING" }
                            )
                        }
                        "ONBOARDING" -> {
                            OnboardingScreen(
                                language = language,
                                onFinishOnboarding = { currentRoute = "LOGIN" }
                            )
                        }
                        "LOGIN" -> {
                            LoginScreen(
                                language = language,
                                isLoading = isAuthLoading,
                                errorMessage = authError,
                                onEmailLogin = { email, pass, name ->
                                    mainViewModel.loginWithEmail(email, pass, name) {
                                        currentRoute = "MAIN"
                                    }
                                },
                                onEmailSignUp = { email, pass, name ->
                                    mainViewModel.signUpWithEmail(email, pass, name) {
                                        currentRoute = "MAIN"
                                    }
                                },
                                onGoogleLogin = {
                                    mainViewModel.loginWithEmail("google.user@homeworkout.pro", "Pass123!", "Google Athlete") {
                                        currentRoute = "MAIN"
                                    }
                                },
                                onGuestLogin = {
                                    mainViewModel.loginAnonymously {
                                        currentRoute = "MAIN"
                                    }
                                },
                                onForgotPassword = { email ->
                                    mainViewModel.sendPasswordReset(email) {}
                                }
                            )
                        }
                        "CHATBOT" -> {
                            ChatbotScreen(
                                language = language,
                                chatMessages = chatMessages,
                                isLoading = isChatLoading,
                                selectedModel = selectedGeminiModel,
                                onModelSelect = { model -> mainViewModel.selectGeminiModel(model) },
                                onSendMessage = { text -> mainViewModel.sendMessageToGemini(text) },
                                onBack = { currentRoute = "MAIN" }
                            )
                        }
                        "ACTIVE_SESSION" -> {
                            ActiveWorkoutScreen(
                                exercises = activeExercises,
                                sessionTitle = activeSessionTitle,
                                language = language,
                                onSpeakVoice = { msg -> mainViewModel.speakVoiceCoach(msg) },
                                onFinishWorkout = { durationSec, calories, count ->
                                    lastDuration = durationSec
                                    lastCalories = calories
                                    lastExerciseCount = count
                                    mainViewModel.recordCompletedWorkout(
                                        activeSessionTitle,
                                        "Home Workout",
                                        durationSec,
                                        calories,
                                        count
                                    )
                                    currentRoute = "COMPLETE"
                                },
                                onCancelWorkout = { currentRoute = "MAIN" }
                            )
                        }
                        "COMPLETE" -> {
                            WorkoutCompleteScreen(
                                durationSec = lastDuration,
                                calories = lastCalories,
                                exerciseCount = lastExerciseCount,
                                language = language,
                                onReturnHome = { currentRoute = "MAIN" }
                            )
                        }
                        "ADMIN" -> {
                            AdminPanelScreen(
                                language = language,
                                customExercises = customExercises,
                                onAddCustomExercise = { nameEn, nameNp, cat, muscle, diff, cal, dur, reps, video ->
                                    mainViewModel.addCustomExercise(nameEn, nameNp, cat, muscle, diff, cal, dur, reps, video)
                                },
                                onDeleteCustomExercise = { id -> mainViewModel.deleteCustomExercise(id) },
                                onSendNotification = { title, msg -> mainViewModel.sendAdminNotification(title, msg) },
                                onBack = { currentRoute = "MAIN" }
                            )
                        }
                        "MAIN" -> {
                            Scaffold(
                                floatingActionButton = {
                                    ExtendedFloatingActionButton(
                                        onClick = { currentRoute = "CHATBOT" },
                                        icon = { Icon(Icons.Default.SmartToy, contentDescription = "AI Coach") },
                                        text = { Text("AI Coach", fontWeight = FontWeight.Bold) },
                                        containerColor = PrimaryOrange,
                                        contentColor = Color.White
                                    )
                                },
                                bottomBar = {
                                    NavigationBar(
                                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                        windowInsets = WindowInsets.navigationBars
                                    ) {
                                        NavigationBarItem(
                                            selected = mainTab == 0,
                                            onClick = { mainTab = 0 },
                                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                            label = { Text(L10n.getString("home", language)) },
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = PrimaryOrange,
                                                indicatorColor = PrimaryOrange.copy(alpha = 0.15f)
                                            )
                                        )
                                        NavigationBarItem(
                                            selected = mainTab == 1,
                                            onClick = { mainTab = 1 },
                                            icon = { Icon(Icons.Default.FitnessCenter, contentDescription = "Workouts") },
                                            label = { Text(L10n.getString("workouts", language)) },
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = PrimaryOrange,
                                                indicatorColor = PrimaryOrange.copy(alpha = 0.15f)
                                            )
                                        )
                                        NavigationBarItem(
                                            selected = mainTab == 2,
                                            onClick = { mainTab = 2 },
                                            icon = { Icon(Icons.Default.ShowChart, contentDescription = "Tools") },
                                            label = { Text(L10n.getString("tools", language)) },
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = PrimaryOrange,
                                                indicatorColor = PrimaryOrange.copy(alpha = 0.15f)
                                            )
                                        )
                                        NavigationBarItem(
                                            selected = mainTab == 3,
                                            onClick = { mainTab = 3 },
                                            icon = { Icon(Icons.Default.Restaurant, contentDescription = "Diet") },
                                            label = { Text(L10n.getString("diet", language)) },
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = PrimaryOrange,
                                                indicatorColor = PrimaryOrange.copy(alpha = 0.15f)
                                            )
                                        )
                                        NavigationBarItem(
                                            selected = mainTab == 4,
                                            onClick = { mainTab = 4 },
                                            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                                            label = { Text(L10n.getString("profile", language)) },
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = PrimaryOrange,
                                                indicatorColor = PrimaryOrange.copy(alpha = 0.15f)
                                            )
                                        )
                                    }
                                }
                            ) { paddingValues ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(paddingValues)
                                ) {
                                    when (mainTab) {
                                        0 -> HomeScreen(
                                            userProfile = userProfile,
                                            language = language,
                                            onCategoryClick = { cat ->
                                                selectedCategoryFilter = cat
                                                mainTab = 1
                                            },
                                            onChallengeClick = { challenge ->
                                                activeExercises = ExercisesData.allExercises.take(10)
                                                activeSessionTitle = if (language == Language.NEPALI) challenge.nameNp else challenge.nameEn
                                                currentRoute = "ACTIVE_SESSION"
                                            },
                                            onStartDailyWorkout = {
                                                activeExercises = ExercisesData.allExercises.take(12)
                                                activeSessionTitle = L10n.getString("daily_workout", language)
                                                currentRoute = "ACTIVE_SESSION"
                                            },
                                            onLanguageToggle = {
                                                val next = if (language == Language.ENGLISH) Language.NEPALI else Language.ENGLISH
                                                mainViewModel.setLanguage(next)
                                            }
                                        )
                                        1 -> WorkoutListScreen(
                                            initialCategory = selectedCategoryFilter,
                                            language = language,
                                            onStartWorkoutSession = { exercises, title ->
                                                activeExercises = exercises
                                                activeSessionTitle = title
                                                currentRoute = "ACTIVE_SESSION"
                                            }
                                        )
                                        2 -> TrackerScreen(
                                            language = language,
                                            currentSteps = stepsCount,
                                            waterLogs = waterLogs,
                                            weightLogs = weightLogs,
                                            bodyMeasurements = bodyMeasurements,
                                            onAddWater = { amount -> mainViewModel.addWater(amount) },
                                            onResetWater = { mainViewModel.resetWater() },
                                            onLogWeight = { w -> mainViewModel.logWeight(w) },
                                            onLogMeasurements = { c, w, b, t, h ->
                                                mainViewModel.logBodyMeasurement(c, w, b, t, h)
                                            },
                                            onSimulateSteps = { mainViewModel.simulateWalkSteps() }
                                        )
                                        3 -> DietScreen(language = language)
                                        4 -> ProfileScreen(
                                            userProfile = userProfile,
                                            workoutLogs = workoutLogs,
                                            language = language,
                                            isDarkMode = isDarkMode,
                                            onLanguageChange = { lang -> mainViewModel.setLanguage(lang) },
                                            onToggleTheme = { mainViewModel.toggleThemeMode() },
                                            onOpenAdminPanel = { currentRoute = "ADMIN" }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
