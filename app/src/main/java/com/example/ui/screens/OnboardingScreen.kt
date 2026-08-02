package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.L10n
import com.example.data.model.Language
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.SecondaryCyan

@Composable
fun OnboardingScreen(
    language: Language,
    onFinishOnboarding: () -> Unit
) {
    var pageIndex by remember { mutableStateOf(0) }

    val pages = listOf(
        OnboardingItem(
            titleEn = "No Equipment Required",
            titleNp = "कुनै पनि सामान चाहिँदैन",
            subEn = "Sculpt your dream physique at home with 100% bodyweight workouts guided by AI voice coach.",
            subNp = "AI आवाज कोचको सहयोगमा घरमै केवल आफ्नो वजन प्रयोग गरेर आकर्षक शरीर बनाउनुहोस्।",
            imageRes = R.drawable.img_hero_banner_1785671467723
        ),
        OnboardingItem(
            titleEn = "150+ Exercises & 90-Day Challenges",
            titleNp = "१५०+ व्यायाम र ९०-दिने चुनौतीहरू",
            subEn = "Target full body, chest, arms, abs, legs, back, and cardio with custom beginner to pro plans.",
            subNp = "छाती, हात, पेट, खुट्टा र कार्डियोका लागि विशेष योजनाहरू प्रयोग गर्नुहोस्।",
            imageRes = R.drawable.img_diet_meal_1785671486642
        ),
        OnboardingItem(
            titleEn = "Track Weight, Water & Earn XP",
            titleNp = "तौल, पानी ट्र्याक र XP लेभल",
            subEn = "Stay motivated with daily streaks, level up your XP avatar, and calculate BMI & nutrition macros.",
            subNp = "दैनिक स्ट्रिक कायम राख्नुहोस्, XP लेभल बढाउनुहोस् र BMI तथा आहार ट्र्याक गर्नुहोस्।",
            imageRes = R.drawable.img_hero_banner_1785671467723
        )
    )

    val current = pages[pageIndex]

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onFinishOnboarding) {
                    Text("Skip", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                }
            }

            // Image Hero Frame
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = current.imageRes),
                    contentDescription = "Onboarding Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Text Block
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (language == Language.NEPALI) current.titleNp else current.titleEn,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (language == Language.NEPALI) current.subNp else current.subEn,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }

            // Bottom Navigation & Dots
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Page Indicators
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(pages.size) { idx ->
                        Box(
                            modifier = Modifier
                                .size(if (idx == pageIndex) 24.dp else 8.dp, 8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (idx == pageIndex) PrimaryOrange else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                                )
                        )
                    }
                }

                // Next Button
                Button(
                    onClick = {
                        if (pageIndex < pages.size - 1) {
                            pageIndex++
                        } else {
                            onFinishOnboarding()
                        }
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = if (pageIndex == pages.size - 1) L10n.getString("get_started", language) else "Next",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

private data class OnboardingItem(
    val titleEn: String,
    val titleNp: String,
    val subEn: String,
    val subNp: String,
    val imageRes: Int
)
