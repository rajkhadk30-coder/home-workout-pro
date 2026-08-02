package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.SecondaryCyan

@Composable
fun ExerciseAnimationView(
    animationType: String,
    isPlaying: Boolean = true,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "WorkoutPoseTransition")
    
    val phaseProgress by if (isPlaying) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200, easing = LinearOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "PhaseAnimation"
        )
    } else {
        rememberUpdatedState(0.5f)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surfaceContainer,
                        MaterialTheme.colorScheme.surface
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Glowing background energy ring
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = 80.dp.toPx()
            drawCircle(
                color = PrimaryOrange.copy(alpha = 0.12f),
                radius = radius + (phaseProgress * 15.dp.toPx()),
                center = center
            )
            drawCircle(
                color = SecondaryCyan.copy(alpha = 0.08f),
                radius = radius + 30.dp.toPx(),
                center = center
            )
        }

        // Animated Body Stick Figure Pose
        Canvas(modifier = Modifier.size(200.dp, 180.dp)) {
            val w = size.width
            val h = size.height
            val primaryColor = PrimaryOrange
            val accentColor = SecondaryCyan
            val strokeWidthVal = 8.dp.toPx()

            when (animationType.uppercase()) {
                "PUSHUP" -> {
                    // Pushup Pose: Y shifts between up and down
                    val yOffset = phaseProgress * 35.dp.toPx()
                    val head = Offset(w * 0.25f, h * 0.35f + yOffset)
                    val shoulder = Offset(w * 0.35f, h * 0.42f + yOffset)
                    val hip = Offset(w * 0.65f, h * 0.48f + yOffset * 0.6f)
                    val feet = Offset(w * 0.88f, h * 0.55f)
                    val hand = Offset(w * 0.35f, h * 0.70f)

                    // Draw Head
                    drawCircle(color = primaryColor, radius = 14.dp.toPx(), center = head)
                    // Spine
                    drawLine(color = primaryColor, start = head, end = feet, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    // Arms (bending)
                    drawLine(color = accentColor, start = shoulder, end = Offset(w * 0.30f, h * 0.55f + yOffset * 0.5f), strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = accentColor, start = Offset(w * 0.30f, h * 0.55f + yOffset * 0.5f), end = hand, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                }
                "SQUAT" -> {
                    // Squat Pose: Hips drop down
                    val squatFactor = phaseProgress * 40.dp.toPx()
                    val head = Offset(w * 0.5f, h * 0.22f + squatFactor)
                    val hip = Offset(w * 0.5f, h * 0.50f + squatFactor)
                    val knee = Offset(w * 0.65f - squatFactor * 0.3f, h * 0.65f + squatFactor * 0.5f)
                    val feet = Offset(w * 0.5f, h * 0.85f)

                    drawCircle(color = primaryColor, radius = 14.dp.toPx(), center = head)
                    drawLine(color = primaryColor, start = head, end = hip, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    // Legs
                    drawLine(color = accentColor, start = hip, end = knee, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    drawLine(color = accentColor, start = knee, end = feet, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                }
                "JUMPING_JACKS" -> {
                    // Arms & Legs spreading wide
                    val spread = phaseProgress * 30.dp.toPx()
                    val head = Offset(w * 0.5f, h * 0.2f - (phaseProgress * 10.dp.toPx()))
                    val hip = Offset(w * 0.5f, h * 0.55f)

                    drawCircle(color = primaryColor, radius = 14.dp.toPx(), center = head)
                    drawLine(color = primaryColor, start = head, end = hip, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    // Left Arm up/down
                    drawLine(color = accentColor, start = Offset(w * 0.5f, h * 0.3f), end = Offset(w * 0.2f - spread * 0.5f, h * 0.2f - spread), strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    // Right Arm up/down
                    drawLine(color = accentColor, start = Offset(w * 0.5f, h * 0.3f), end = Offset(w * 0.8f + spread * 0.5f, h * 0.2f - spread), strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    // Left Leg
                    drawLine(color = primaryColor, start = hip, end = Offset(w * 0.35f - spread, h * 0.85f), strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                    // Right Leg
                    drawLine(color = primaryColor, start = hip, end = Offset(w * 0.65f + spread, h * 0.85f), strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                }
                else -> {
                    // Default Plank / Core pose
                    val head = Offset(w * 0.2f, h * 0.45f)
                    val feet = Offset(w * 0.85f, h * 0.50f)
                    drawCircle(color = primaryColor, radius = 14.dp.toPx(), center = head)
                    drawLine(color = primaryColor, start = head, end = feet, strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                }
            }
        }

        // Live HD Animation Pill Tag
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .clip(CircleShape)
                .background(PrimaryOrange.copy(alpha = 0.9f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "HD ANIMATED COACH",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
