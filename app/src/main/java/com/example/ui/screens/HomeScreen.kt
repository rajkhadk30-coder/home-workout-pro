package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.UserProfileEntity
import com.example.data.model.*
import com.example.data.repository.ExercisesData
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userProfile: UserProfileEntity,
    language: Language,
    onCategoryClick: (ExerciseCategory) -> Unit,
    onChallengeClick: (ChallengePlan) -> Unit,
    onStartDailyWorkout: () -> Unit,
    onLanguageToggle: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // 1. Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(PrimaryOrange)
                        .padding(2.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_app_icon_1785671425164),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Hello, ${userProfile.name} 👋",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = L10n.getString("welcome_back", language),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            // Language & Streak Badge
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Streak Pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = PrimaryOrange.copy(alpha = 0.15f),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Streak",
                            tint = PrimaryOrange,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${userProfile.currentStreak} ${L10n.getString("days", language)}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryOrange
                        )
                    }
                }

                // Language Button
                IconButton(onClick = onLanguageToggle) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceContainer
                    ) {
                        Box(
                            modifier = Modifier.padding(6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (language == Language.ENGLISH) "🇳🇵" else "🇺🇸",
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 2. XP & Level Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MilitaryTech,
                            contentDescription = "Level",
                            tint = AccentGold,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${L10n.getString("level", language)} ${userProfile.level} Athlete",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        text = "${userProfile.xp} XP",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = SecondaryCyan
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // XP Progress Bar
                val progress = ((userProfile.xp % 200) / 200f).coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = PrimaryOrange,
                    trackColor = PrimaryOrange.copy(alpha = 0.2f)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3. Featured Daily Workout Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(24.dp))
                .clickable { onStartDailyWorkout() }
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_banner_1785671467723),
                contentDescription = "Daily Workout Banner",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            // Content inside banner
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = PrimaryOrange
                ) {
                    Text(
                        text = L10n.getString("daily_workout", language).uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Full Body Energy Shred",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Text(
                    text = "12 Exercises • 15 Mins • 180 Kcal",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4. Challenges Section (7, 14, 30, 60, 90 Days)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = L10n.getString("challenges", language),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        val challengePlans = listOf(
            ChallengeItem("7-Day Jumpstart", "७-दिने जम्पस्टार्ट", "7 Days", Difficulty.BEGINNER, PrimaryOrange),
            ChallengeItem("14-Day Fat Burner", "१४-दिने बोसो पगाल्ने", "14 Days", Difficulty.INTERMEDIATE, SecondaryCyan),
            ChallengeItem("30-Day Full Body Shred", "३०-दिने रुपान्तरण", "30 Days", Difficulty.INTERMEDIATE, AccentGold),
            ChallengeItem("60-Day Muscle Beast", "६०-दिने बिस्ट", "60 Days", Difficulty.ADVANCED, AccentGreen),
            ChallengeItem("90-Day Master Challenge", "९०-दिने मास्टर", "90 Days", Difficulty.ADVANCED, AccentRed)
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(challengePlans) { challenge ->
                Card(
                    modifier = Modifier
                        .width(180.dp)
                        .height(140.dp)
                        .clickable {
                            val plan = ChallengePlan(
                                id = challenge.titleEn,
                                nameEn = challenge.titleEn,
                                nameNp = challenge.titleNp,
                                durationDays = challenge.duration.replace(" Days", "").toIntOrNull() ?: 30,
                                level = challenge.level,
                                descriptionEn = "Full structured multi-day challenge",
                                descriptionNp = "विशेष बहु-दिने चुनौती कार्यक्रम",
                                exerciseIds = listOf("fb_1", "chest_1", "abs_1", "leg_1")
                            )
                            onChallengeClick(plan)
                        },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = challenge.accentColor.copy(alpha = 0.2f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FitnessCenter,
                                    contentDescription = null,
                                    tint = challenge.accentColor,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .size(20.dp)
                                )
                            }

                            Text(
                                text = challenge.duration,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = challenge.accentColor
                            )
                        }

                        Column {
                            Text(
                                text = if (language == Language.NEPALI) challenge.titleNp else challenge.titleEn,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = if (language == Language.NEPALI) challenge.level.labelNp else challenge.level.labelEn,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 5. Workout Categories Grid (11 Categories)
        Text(
            text = L10n.getString("workout_categories", language),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        val categories = ExerciseCategory.values().toList()

        // 2 Column Grid for categories
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            categories.chunked(2).forEach { rowCategories ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowCategories.forEach { cat ->
                        CategoryGridCard(
                            cat = cat,
                            language = language,
                            modifier = Modifier.weight(1f),
                            onCategoryClick = onCategoryClick
                        )
                    }
                    if (rowCategories.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryGridCard(
    cat: ExerciseCategory,
    language: Language,
    modifier: Modifier = Modifier,
    onCategoryClick: (ExerciseCategory) -> Unit
) {
    val count = ExercisesData.allExercises.count { it.category == cat }
    Card(
        modifier = modifier
            .height(90.dp)
            .clickable { onCategoryClick(cat) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = PrimaryOrange.copy(alpha = 0.15f)
            ) {
                Icon(
                    imageVector = getCategoryIcon(cat),
                    contentDescription = cat.name,
                    tint = PrimaryOrange,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = if (language == Language.NEPALI) cat.displayNameNp else cat.displayNameEn,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$count ${L10n.getString("exercise_count", language)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

private data class ChallengeItem(
    val titleEn: String,
    val titleNp: String,
    val duration: String,
    val level: Difficulty,
    val accentColor: Color
)

fun getCategoryIcon(cat: ExerciseCategory): androidx.compose.ui.graphics.vector.ImageVector {
    return when (cat) {
        ExerciseCategory.FULL_BODY -> Icons.Default.DirectionsRun
        ExerciseCategory.CHEST -> Icons.Default.FitnessCenter
        ExerciseCategory.ARMS -> Icons.Default.SportsGymnastics
        ExerciseCategory.ABS -> Icons.Default.SelfImprovement
        ExerciseCategory.LEGS -> Icons.Default.DirectionsWalk
        ExerciseCategory.BACK -> Icons.Default.AccessibilityNew
        ExerciseCategory.SHOULDERS -> Icons.Default.SportsMma
        ExerciseCategory.CARDIO -> Icons.Default.Favorite
        ExerciseCategory.HIIT -> Icons.Default.Whatshot
        ExerciseCategory.STRETCHING -> Icons.Default.Spa
        ExerciseCategory.WARM_UP -> Icons.Default.WbSunny
    }
}
