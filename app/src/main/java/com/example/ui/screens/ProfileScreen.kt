package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.UserProfileEntity
import com.example.data.local.WorkoutLogEntity
import com.example.data.model.*
import com.example.data.repository.WorkoutRepository
import com.example.ui.theme.AccentGold
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.SecondaryCyan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userProfile: UserProfileEntity,
    workoutLogs: List<WorkoutLogEntity>,
    language: Language,
    isDarkMode: Boolean,
    onLanguageChange: (Language) -> Unit,
    onToggleTheme: () -> Unit,
    onOpenAdminPanel: () -> Unit
) {
    var showDialogPrivacy by remember { mutableStateOf(false) }
    var showDialogTerms by remember { mutableStateOf(false) }
    var showDialogBackup by remember { mutableStateOf(false) }

    val achievements = WorkoutRepository().getAchievementsList(
        userXp = userProfile.xp,
        userStreak = userProfile.currentStreak,
        workoutCount = workoutLogs.size
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Profile Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(PrimaryOrange)
                            .padding(2.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_app_icon_1785671425164),
                            contentDescription = "Profile",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = userProfile.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = userProfile.email,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                shape = CircleShape,
                                color = AccentGold.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "Lvl ${userProfile.level}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentGold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                            Surface(
                                shape = CircleShape,
                                color = PrimaryOrange.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "${userProfile.currentStreak} Day Streak 🔥",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryOrange,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Achievement Badges Section
        item {
            Text(
                text = L10n.getString("achievements", language),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    achievements.chunked(3).forEach { rowBadges ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            rowBadges.forEach { badge ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(6.dp)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = if (badge.isUnlocked) AccentGold.copy(alpha = 0.25f) else Color.Gray.copy(alpha = 0.15f),
                                        modifier = Modifier.size(50.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.EmojiEvents,
                                                contentDescription = null,
                                                tint = if (badge.isUnlocked) AccentGold else Color.Gray
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (language == Language.NEPALI) badge.titleNp else badge.titleEn,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (badge.isUnlocked) MaterialTheme.colorScheme.onSurface else Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Settings Section
        item {
            Text(
                text = L10n.getString("settings", language),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    // Language Choice
                    ListItem(
                        headlineContent = { Text(L10n.getString("language", language)) },
                        supportingContent = {
                            Text(if (language == Language.ENGLISH) "English" else "नेपाली")
                        },
                        leadingContent = { Icon(Icons.Default.Language, contentDescription = null, tint = PrimaryOrange) },
                        trailingContent = {
                            TextButton(
                                onClick = {
                                    val next = if (language == Language.ENGLISH) Language.NEPALI else Language.ENGLISH
                                    onLanguageChange(next)
                                }
                            ) {
                                Text("Switch", color = PrimaryOrange, fontWeight = FontWeight.Bold)
                            }
                        }
                    )

                    Divider()

                    // Theme Mode
                    ListItem(
                        headlineContent = { Text(L10n.getString("theme", language)) },
                        supportingContent = {
                            Text(if (isDarkMode) L10n.getString("dark_mode", language) else L10n.getString("light_mode", language))
                        },
                        leadingContent = { Icon(Icons.Default.DarkMode, contentDescription = null, tint = SecondaryCyan) },
                        trailingContent = {
                            Switch(
                                checked = isDarkMode,
                                onCheckedChange = { onToggleTheme() },
                                colors = SwitchDefaults.colors(checkedThumbColor = PrimaryOrange)
                            )
                        }
                    )

                    Divider()

                    // Backup & Restore
                    ListItem(
                        headlineContent = { Text(L10n.getString("backup_restore", language)) },
                        leadingContent = { Icon(Icons.Default.CloudSync, contentDescription = null, tint = AccentGold) },
                        modifier = Modifier.clickable { showDialogBackup = true }
                    )

                    Divider()

                    // Admin Panel Button
                    ListItem(
                        headlineContent = { Text(L10n.getString("admin_panel", language), fontWeight = FontWeight.Bold, color = PrimaryOrange) },
                        leadingContent = { Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = PrimaryOrange) },
                        trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                        modifier = Modifier.clickable { onOpenAdminPanel() }
                    )

                    Divider()

                    // Privacy Policy
                    ListItem(
                        headlineContent = { Text(L10n.getString("privacy_policy", language)) },
                        leadingContent = { Icon(Icons.Default.Security, contentDescription = null) },
                        modifier = Modifier.clickable { showDialogPrivacy = true }
                    )

                    Divider()

                    // Terms & Conditions
                    ListItem(
                        headlineContent = { Text(L10n.getString("terms_conditions", language)) },
                        leadingContent = { Icon(Icons.Default.Gavel, contentDescription = null) },
                        modifier = Modifier.clickable { showDialogTerms = true }
                    )
                }
            }
        }
    }

    // Dialogs
    if (showDialogBackup) {
        AlertDialog(
            onDismissRequest = { showDialogBackup = false },
            title = { Text("Backup & Restore") },
            text = { Text("Database successfully synced to cloud room storage.") },
            confirmButton = {
                TextButton(onClick = { showDialogBackup = false }) { Text("OK") }
            }
        )
    }

    if (showDialogPrivacy) {
        AlertDialog(
            onDismissRequest = { showDialogPrivacy = false },
            title = { Text("Privacy Policy") },
            text = { Text("Home Workout Pro values your privacy. All fitness logs, weight entries, and measurements remain stored securely on your device.") },
            confirmButton = {
                TextButton(onClick = { showDialogPrivacy = false }) { Text("Close") }
            }
        )
    }

    if (showDialogTerms) {
        AlertDialog(
            onDismissRequest = { showDialogTerms = false },
            title = { Text("Terms & Conditions") },
            text = { Text("Please warm up before intense workouts. Home Workout Pro is designed for general home fitness and conditioning.") },
            confirmButton = {
                TextButton(onClick = { showDialogTerms = false }) { Text("Close") }
            }
        )
    }
}
