package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Exercise
import com.example.data.model.L10n
import com.example.data.model.Language
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.SecondaryCyan
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveWorkoutScreen(
    exercises: List<Exercise>,
    sessionTitle: String,
    language: Language,
    onSpeakVoice: (String) -> Unit,
    onFinishWorkout: (durationSec: Int, totalCalories: Int, count: Int) -> Unit,
    onCancelWorkout: () -> Unit
) {
    if (exercises.isEmpty()) {
        onCancelWorkout()
        return
    }

    var currentIndex by remember { mutableStateOf(0) }
    val currentExercise = exercises[currentIndex]

    var isPaused by remember { mutableStateOf(false) }
    var isResting by remember { mutableStateOf(false) }

    var exerciseTimer by remember { mutableStateOf(currentExercise.durationSeconds) }
    var restTimer by remember { mutableStateOf(currentExercise.restTimeSeconds) }

    var totalElapsedSeconds by remember { mutableStateOf(0) }
    var totalCaloriesBurned by remember { mutableStateOf(0) }

    // Initial voice prompt when starting exercise
    LaunchedEffect(currentIndex, isResting) {
        if (isResting) {
            val restMsg = if (language == Language.NEPALI) "आराम गर्नुहोस्, १५ सेकेन्ड" else "Take a rest for ${currentExercise.restTimeSeconds} seconds"
            onSpeakVoice(restMsg)
            restTimer = currentExercise.restTimeSeconds
        } else {
            val voiceMsg = if (language == Language.NEPALI) currentExercise.voiceGuidanceNp else currentExercise.voiceGuidanceEn
            onSpeakVoice("Ready! $voiceMsg")
            exerciseTimer = currentExercise.durationSeconds
        }
    }

    // Main Timer Loop
    LaunchedEffect(isPaused, isResting, currentIndex) {
        while (!isPaused) {
            delay(1000)
            totalElapsedSeconds++

            if (isResting) {
                if (restTimer > 1) {
                    restTimer--
                } else {
                    isResting = false
                    if (currentIndex < exercises.size - 1) {
                        currentIndex++
                    } else {
                        onFinishWorkout(totalElapsedSeconds, totalCaloriesBurned, exercises.size)
                        break
                    }
                }
            } else {
                if (exerciseTimer > 1) {
                    exerciseTimer--
                } else {
                    totalCaloriesBurned += currentExercise.caloriesBurned
                    if (currentIndex < exercises.size - 1) {
                        isResting = true
                    } else {
                        onFinishWorkout(totalElapsedSeconds, totalCaloriesBurned, exercises.size)
                        break
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(sessionTitle, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onCancelWorkout) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Queue Progress Bar
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Exercise ${currentIndex + 1} of ${exercises.size}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryOrange
                        )
                        Text(
                            text = "${totalElapsedSeconds / 60}m ${totalElapsedSeconds % 60}s • $totalCaloriesBurned Kcal",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { (currentIndex + 1) / exercises.size.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = PrimaryOrange,
                        trackColor = PrimaryOrange.copy(alpha = 0.2f)
                    )
                }

                if (isResting) {
                    // REST OVERLAY
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = L10n.getString("rest_time", language),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SecondaryCyan
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "$restTimer",
                                style = MaterialTheme.typography.displayLarge,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 64.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Button(
                                    onClick = { restTimer += 20 },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                                ) {
                                    Text(L10n.getString("add_rest", language), color = SecondaryCyan)
                                }

                                Button(
                                    onClick = {
                                        isResting = false
                                        if (currentIndex < exercises.size - 1) {
                                            currentIndex++
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
                                ) {
                                    Text(L10n.getString("skip_rest", language), color = Color.White)
                                }
                            }
                        }
                    }
                } else {
                    // ACTIVE EXERCISE DISPLAY
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Animated HD Exercise Graphic
                        ExerciseAnimationView(
                            animationType = currentExercise.animationType,
                            isPlaying = !isPaused
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Exercise Name & Target Muscle
                        Text(
                            text = if (language == Language.NEPALI) currentExercise.nameNp else currentExercise.nameEn,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Surface(
                            shape = CircleShape,
                            color = SecondaryCyan.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "Target: ${currentExercise.targetMuscle} • x${currentExercise.repetitions} Reps",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = SecondaryCyan,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Big Timer Ring Display
                        Text(
                            text = "$exerciseTimer",
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 60.sp,
                            color = PrimaryOrange
                        )
                    }
                }

                // Bottom Session Action Controls
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Previous
                    IconButton(
                        onClick = {
                            if (currentIndex > 0) currentIndex--
                        },
                        enabled = currentIndex > 0
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Previous",
                            tint = if (currentIndex > 0) MaterialTheme.colorScheme.onSurface else Color.Gray,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    // Play/Pause
                    FloatingActionButton(
                        onClick = { isPaused = !isPaused },
                        containerColor = PrimaryOrange,
                        contentColor = Color.White,
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                            contentDescription = "Pause",
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Next / Skip
                    IconButton(
                        onClick = {
                            if (currentIndex < exercises.size - 1) {
                                currentIndex++
                            } else {
                                onFinishWorkout(totalElapsedSeconds, totalCaloriesBurned, exercises.size)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Next",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }
        }
    }
}
