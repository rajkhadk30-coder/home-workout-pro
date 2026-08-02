package com.example.ui.screens

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun ExerciseAnimationView(
    animationType: String,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "exercise_anim")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "anim_progress"
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val accentColor = MaterialTheme.colorScheme.tertiary

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .testTag("exercise_animation_container"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)

            when (animationType.lowercase()) {
                "pushup" -> {
                    val offsetY = (progress * 30.dp.toPx())
                    // Torso
                    drawLine(
                        color = primaryColor,
                        start = Offset(center.x - 60.dp.toPx(), center.y + offsetY),
                        end = Offset(center.x + 60.dp.toPx(), center.y + 15.dp.toPx() + offsetY),
                        strokeWidth = 12.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                    // Head
                    drawCircle(
                        color = accentColor,
                        radius = 16.dp.toPx(),
                        center = Offset(center.x - 80.dp.toPx(), center.y - 10.dp.toPx() + offsetY)
                    )
                }
                "squat" -> {
                    val squatY = (progress * 40.dp.toPx())
                    // Torso
                    drawLine(
                        color = primaryColor,
                        start = Offset(center.x, center.y - 40.dp.toPx() + squatY),
                        end = Offset(center.x, center.y + 20.dp.toPx() + squatY),
                        strokeWidth = 14.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                    // Head
                    drawCircle(
                        color = accentColor,
                        radius = 18.dp.toPx(),
                        center = Offset(center.x, center.y - 65.dp.toPx() + squatY)
                    )
                }
                else -> {
                    // Default Pulsing Figure
                    val radius = (35.dp.toPx() + progress * 15.dp.toPx())
                    drawCircle(
                        color = primaryColor.copy(alpha = 0.8f),
                        radius = radius,
                        center = center
                    )
                    drawCircle(
                        color = accentColor,
                        radius = 16.dp.toPx(),
                        center = center
                    )
                }
            }
        }
    }
}
