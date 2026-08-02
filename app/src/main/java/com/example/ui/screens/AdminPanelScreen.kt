package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.local.CustomExerciseEntity
import com.example.data.model.L10n
import com.example.data.model.Language
import com.example.ui.theme.AccentGold
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.SecondaryCyan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    language: Language,
    customExercises: List<CustomExerciseEntity>,
    onAddCustomExercise: (nameEn: String, nameNp: String, cat: String, muscle: String, diff: String, cal: Int, dur: Int, reps: Int, video: String) -> Unit,
    onDeleteCustomExercise: (Long) -> Unit,
    onSendNotification: (String, String) -> Unit,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Analytics, 1: Exercises, 2: Notifications

    // Custom Exercise Form
    var nameEnInput by remember { mutableStateOf("") }
    var nameNpInput by remember { mutableStateOf("") }
    var categoryInput by remember { mutableStateOf("Chest") }
    var muscleInput by remember { mutableStateOf("Pectorals") }
    var difficultyInput by remember { mutableStateOf("Beginner") }
    var caloriesInput by remember { mutableStateOf("15") }
    var videoUrlInput by remember { mutableStateOf("https://youtube.com/watch?v=workout") }

    // Notification Form
    var notifTitle by remember { mutableStateOf("Time for Workout! 🔥") }
    var notifMsg by remember { mutableStateOf("Keep your 7-day streak going! 15 minutes full body workout waiting.") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(L10n.getString("admin_panel", language), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Tab Selector
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Analytics") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Manage Exercises") }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Notifications") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {
                0 -> {
                    // Analytics Dashboard
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Total Active Users", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                                Text("12,450", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = PrimaryOrange)
                            }
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Workouts Completed Today", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                                Text("3,820", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = SecondaryCyan)
                            }
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Server Database Status", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                                Text("Supabase / Firebase Connected ✅", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AccentGold)
                            }
                        }
                    }
                }
                1 -> {
                    // Manage Exercises
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Add Custom Exercise / Video Link", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(
                                    value = nameEnInput,
                                    onValueChange = { nameEnInput = it },
                                    label = { Text("Exercise Name (English)") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = nameNpInput,
                                    onValueChange = { nameNpInput = it },
                                    label = { Text("Exercise Name (Nepali)") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = videoUrlInput,
                                    onValueChange = { videoUrlInput = it },
                                    label = { Text("Video / Animation URL") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
                                    onClick = {
                                        if (nameEnInput.isNotBlank()) {
                                            onAddCustomExercise(
                                                nameEnInput,
                                                nameNpInput.ifBlank { nameEnInput },
                                                categoryInput,
                                                muscleInput,
                                                difficultyInput,
                                                caloriesInput.toIntOrNull() ?: 15,
                                                30,
                                                15,
                                                videoUrlInput
                                            )
                                            nameEnInput = ""
                                            nameNpInput = ""
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
                                ) {
                                    Text("Add to Database", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Admin Custom Exercises (${customExercises.size})", fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.height(8.dp))

                        customExercises.forEach { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(item.nameEn, fontWeight = FontWeight.Bold)
                                        Text("${item.category} • ${item.caloriesBurned} Kcal", style = MaterialTheme.typography.labelSmall)
                                    }

                                    IconButton(onClick = { onDeleteCustomExercise(item.id) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // Send Push Notification
                    Column(modifier = Modifier.fillMaxSize()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Send Broadcast Push Notification", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                                Spacer(modifier = Modifier.height(12.dp))

                                OutlinedTextField(
                                    value = notifTitle,
                                    onValueChange = { notifTitle = it },
                                    label = { Text("Notification Title") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(
                                    value = notifMsg,
                                    onValueChange = { notifMsg = it },
                                    label = { Text("Message Content") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        onSendNotification(notifTitle, notifMsg)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
                                ) {
                                    Icon(Icons.Default.Send, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Broadcast to All Users", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
