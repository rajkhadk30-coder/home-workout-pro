package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel

@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfile.collectAsState()
    var isNotificationsEnabled by remember { mutableStateOf(true) }
    var isDarkMode by remember { mutableStateOf(true) }

    var nameText by remember { mutableStateOf(profile.name) }
    var weightText by remember { mutableStateOf(profile.weightKg.toString()) }
    var heightText by remember { mutableStateOf(profile.heightCm.toString()) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "My Profile",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Profile Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("user_profile_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = nameText,
                        onValueChange = {
                            nameText = it
                            viewModel.updateUserProfile(profile.copy(name = it))
                        },
                        label = { Text("Display Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("profile_name_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = weightText,
                            onValueChange = {
                                weightText = it
                                it.toFloatOrNull()?.let { w -> viewModel.updateUserProfile(profile.copy(weightKg = w)) }
                            },
                            label = { Text("Weight (kg)") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("profile_weight_input"),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = heightText,
                            onValueChange = {
                                heightText = it
                                it.toFloatOrNull()?.let { h -> viewModel.updateUserProfile(profile.copy(heightCm = h)) }
                            },
                            label = { Text("Height (cm)") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("profile_height_input"),
                            singleLine = true
                        )
                    }
                }
            }
        }

        // Settings Section
        item {
            Text(
                text = "Preferences & Settings",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column {
                    ListItem(
                        headlineContent = { Text("Workout Reminders") },
                        leadingContent = {
                            Icon(imageVector = Icons.Default.Notifications, contentDescription = null)
                        },
                        trailingContent = {
                            Switch(
                                checked = isNotificationsEnabled,
                                onCheckedChange = { isNotificationsEnabled = it },
                                modifier = Modifier.testTag("notifications_switch")
                            )
                        }
                    )

                    HorizontalDivider()

                    ListItem(
                        headlineContent = { Text("Dark Theme") },
                        leadingContent = {
                            Icon(imageVector = Icons.Default.DarkMode, contentDescription = null)
                        },
                        trailingContent = {
                            Switch(
                                checked = isDarkMode,
                                onCheckedChange = { isDarkMode = it },
                                modifier = Modifier.testTag("dark_theme_switch")
                            )
                        }
                    )

                    HorizontalDivider()

                    ListItem(
                        headlineContent = { Text("Privacy Policy") },
                        leadingContent = {
                            Icon(imageVector = Icons.Default.Security, contentDescription = null)
                        },
                        trailingContent = {
                            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null)
                        }
                    )
                }
            }
        }
    }
}
