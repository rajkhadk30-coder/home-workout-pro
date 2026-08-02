package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.BodyMeasurementEntity
import com.example.data.local.WaterLogEntity
import com.example.data.local.WeightLogEntity
import com.example.data.model.L10n
import com.example.data.model.Language
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.SecondaryCyan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackerScreen(
    language: Language,
    currentSteps: Int,
    waterLogs: List<WaterLogEntity>,
    weightLogs: List<WeightLogEntity>,
    bodyMeasurements: List<BodyMeasurementEntity>,
    onAddWater: (Int) -> Unit,
    onResetWater: () -> Unit,
    onLogWeight: (Float) -> Unit,
    onLogMeasurements: (Float, Float, Float, Float, Float) -> Unit,
    onSimulateSteps: () -> Unit
) {
    val scrollState = rememberScrollState()

    // BMI State
    var heightCmInput by remember { mutableStateOf("172") }
    var weightKgInput by remember { mutableStateOf("68") }
    var bmiResult by remember { mutableStateOf<Float?>(23.0f) }

    // BMR State
    var ageInput by remember { mutableStateOf("24") }
    var bmrResult by remember { mutableStateOf<Float?>(1680f) }

    // Body Measurements Input
    var chestInput by remember { mutableStateOf("96") }
    var waistInput by remember { mutableStateOf("80") }
    var bicepsInput by remember { mutableStateOf("34") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = L10n.getString("trackers", language),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 1. Step Counter Card
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
                        Surface(shape = CircleShape, color = PrimaryOrange.copy(alpha = 0.15f)) {
                            Icon(Icons.Default.DirectionsWalk, contentDescription = null, tint = PrimaryOrange, modifier = Modifier.padding(8.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = L10n.getString("step_counter", language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    TextButton(onClick = onSimulateSteps) {
                        Text("+500 Steps", color = PrimaryOrange, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "$currentSteps",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = L10n.getString("step_goal", language),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                val stepProgress = (currentSteps / 10000f).coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = { stepProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = PrimaryOrange,
                    trackColor = PrimaryOrange.copy(alpha = 0.2f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Water Intake Tracker
        val totalWater = waterLogs.sumOf { it.amountMl }
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
                        Surface(shape = CircleShape, color = SecondaryCyan.copy(alpha = 0.15f)) {
                            Icon(Icons.Default.WaterDrop, contentDescription = null, tint = SecondaryCyan, modifier = Modifier.padding(8.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = L10n.getString("water_tracker", language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(onClick = onResetWater) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "$totalWater ml",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = SecondaryCyan
                    )
                    Text(
                        text = L10n.getString("water_goal", language),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                val waterProgress = (totalWater / 2500f).coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = { waterProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = SecondaryCyan,
                    trackColor = SecondaryCyan.copy(alpha = 0.2f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { onAddWater(250) },
                        colors = ButtonDefaults.buttonColors(containerColor = SecondaryCyan),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("+250ml Glass", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { onAddWater(500) },
                        colors = ButtonDefaults.buttonColors(containerColor = SecondaryCyan.copy(alpha = 0.8f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("+500ml Bottle", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. BMI & BMR Calculators
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = L10n.getString("bmi_calculator", language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = heightCmInput,
                        onValueChange = { heightCmInput = it },
                        label = { Text(L10n.getString("height", language)) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = weightKgInput,
                        onValueChange = { weightKgInput = it },
                        label = { Text(L10n.getString("weight", language)) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val h = heightCmInput.toFloatOrNull() ?: 172f
                        val w = weightKgInput.toFloatOrNull() ?: 68f
                        if (h > 0) {
                            val hMeters = h / 100f
                            bmiResult = w / (hMeters * hMeters)
                            bmrResult = (10 * w) + (6.25f * h) - (5 * 24) + 5
                            onLogWeight(w)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(L10n.getString("calculate", language), color = Color.White, fontWeight = FontWeight.Bold)
                }

                bmiResult?.let { bmi ->
                    Spacer(modifier = Modifier.height(14.dp))
                    val category = when {
                        bmi < 18.5f -> "Underweight" to AccentGold
                        bmi < 24.9f -> "Normal Weight ✅" to AccentGreen
                        bmi < 29.9f -> "Overweight" to PrimaryOrange
                        else -> "Obese" to Color.Red
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "BMI: String.format(\"%.1f\", bmi) -> ${String.format("%.1f", bmi)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = category.first,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = category.second
                        )
                    }
                    bmrResult?.let { bmr ->
                        Text(
                            text = "BMR (Daily Burn): ${bmr.toInt()} Kcal/day",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Body Measurements Tracker
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = L10n.getString("body_measurements", language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = chestInput,
                        onValueChange = { chestInput = it },
                        label = { Text("Chest (cm)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = waistInput,
                        onValueChange = { waistInput = it },
                        label = { Text("Waist (cm)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = bicepsInput,
                        onValueChange = { bicepsInput = it },
                        label = { Text("Biceps (cm)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val c = chestInput.toFloatOrNull() ?: 96f
                        val w = waistInput.toFloatOrNull() ?: 80f
                        val b = bicepsInput.toFloatOrNull() ?: 34f
                        onLogMeasurements(c, w, b, 55f, 92f)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = SecondaryCyan),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save Measurements", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
