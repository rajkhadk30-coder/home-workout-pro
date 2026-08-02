package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.data.repository.ExercisesData
import com.example.ui.theme.AccentGold
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.SecondaryCyan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutListScreen(
    initialCategory: ExerciseCategory? = null,
    language: Language,
    onStartWorkoutSession: (List<Exercise>, title: String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(initialCategory) }
    var selectedDifficulty by remember { mutableStateOf<Difficulty?>(null) }

    val filteredExercises = remember(searchQuery, selectedCategory, selectedDifficulty) {
        ExercisesData.allExercises.filter { ex ->
            val matchesQuery = searchQuery.isBlank() ||
                    ex.nameEn.contains(searchQuery, ignoreCase = true) ||
                    ex.nameNp.contains(searchQuery, ignoreCase = true) ||
                    ex.targetMuscle.contains(searchQuery, ignoreCase = true)
            
            val matchesCategory = selectedCategory == null || ex.category == selectedCategory
            val matchesDifficulty = selectedDifficulty == null || ex.difficulty == selectedDifficulty

            matchesQuery && matchesCategory && matchesDifficulty
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = selectedCategory?.let {
                            if (language == Language.NEPALI) it.displayNameNp else it.displayNameEn
                        } ?: L10n.getString("workouts", language),
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            if (filteredExercises.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = {
                        val sessionTitle = selectedCategory?.let {
                            if (language == Language.NEPALI) it.displayNameNp else it.displayNameEn
                        } ?: "Custom Workout"
                        onStartWorkoutSession(filteredExercises.take(12), sessionTitle)
                    },
                    containerColor = PrimaryOrange,
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Start")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${L10n.getString("start_workout", language)} (${filteredExercises.size.coerceAtMost(12)})",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search exercise, muscle...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category Chips Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        label = { Text("All") }
                    )
                }
                items(ExerciseCategory.values()) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = if (selectedCategory == cat) null else cat },
                        label = {
                            Text(if (language == Language.NEPALI) cat.displayNameNp else cat.displayNameEn)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Difficulty Filters
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Difficulty.values().forEach { diff ->
                    FilterChip(
                        selected = selectedDifficulty == diff,
                        onClick = { selectedDifficulty = if (selectedDifficulty == diff) null else diff },
                        label = { Text(if (language == Language.NEPALI) diff.labelNp else diff.labelEn) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Exercise List
            if (filteredExercises.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No exercises found for this filter.",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(filteredExercises) { exercise ->
                        ExerciseCard(
                            exercise = exercise,
                            language = language,
                            onPlaySingle = {
                                onStartWorkoutSession(listOf(exercise), exercise.nameEn)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExerciseCard(
    exercise: Exercise,
    language: Language,
    onPlaySingle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Exercise Type Icon
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = PrimaryOrange.copy(alpha = 0.15f),
                    modifier = Modifier.size(52.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = getCategoryIcon(exercise.category),
                            contentDescription = null,
                            tint = PrimaryOrange,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = if (language == Language.NEPALI) exercise.nameNp else exercise.nameEn,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${exercise.repetitions} reps • ${exercise.caloriesBurned} Kcal",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Surface(
                            shape = CircleShape,
                            color = SecondaryCyan.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = exercise.targetMuscle,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = SecondaryCyan,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            IconButton(onClick = onPlaySingle) {
                Icon(
                    imageVector = Icons.Default.PlayCircle,
                    contentDescription = "Play Exercise",
                    tint = PrimaryOrange,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}
