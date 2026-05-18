package com.example.reshmenammapride

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.reshmenammapride.ui.theme.ReshmeTheme
import com.example.reshmenammapride.ui.theme.SuccessGreen
import com.example.reshmenammapride.ui.theme.WarningOrange
import com.example.reshmenammapride.ui.theme.DangerRed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ClimateFragment : Fragment() {

    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        db = AppDatabase.getDatabase(requireContext())

        val prefs = requireActivity().getSharedPreferences("ReshmePrefs", Context.MODE_PRIVATE)
        val userEmail = prefs.getString("user_email", "") ?: ""

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ReshmeTheme {
                    ClimateEntryScreen(
                        userEmail = userEmail,
                        db = db,
                        context = requireContext(),
                        onBack = { findNavController().navigateUp() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClimateEntryScreen(userEmail: String, db: AppDatabase, context: android.content.Context, onBack: () -> Unit) {
    var breed by remember { mutableStateOf("") }
    var dayText by remember { mutableStateOf("") }
    var tempText by remember { mutableStateOf("") }
    var humidityText by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<ClimateResult?>(null) }
    var stage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val breeds = listOf("Bivoltine", "Cross Breed", "CSR2 x CSR4", "Pure Mysore", "Double Hybrid")
    val days = (1..100).map { it.toString() }
    val temps = (15..40).map { it.toString() }
    val humidities = (40..100).map { it.toString() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Climate Data", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2E7D32),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
        // Breed Selection
        var breedExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = breedExpanded,
            onExpandedChange = { breedExpanded = !breedExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = breed,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Breed") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = breedExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
                expanded = breedExpanded,
                onDismissRequest = { breedExpanded = false }
            ) {
                breeds.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            breed = item
                            breedExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Day Selection
        var dayExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = dayExpanded,
            onExpandedChange = { dayExpanded = !dayExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = if (dayText.isEmpty()) "" else "$dayText Days",
                onValueChange = {},
                readOnly = true,
                label = { Text("Batch Age") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dayExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
                expanded = dayExpanded,
                onDismissRequest = { dayExpanded = false }
            ) {
                days.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = "$item Days") },
                        onClick = {
                            dayText = item
                            dayExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            // Temp Selection
            var tempExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = tempExpanded,
                onExpandedChange = { tempExpanded = !tempExpanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = if (tempText.isEmpty()) "" else "$tempText°C",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Temp") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tempExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = tempExpanded,
                    onDismissRequest = { tempExpanded = false }
                ) {
                    temps.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = "$item°C") },
                            onClick = {
                                tempText = item
                                tempExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Humidity Selection
            var humExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = humExpanded,
                onExpandedChange = { humExpanded = !humExpanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = if (humidityText.isEmpty()) "" else "$humidityText%",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Humidity") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = humExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = humExpanded,
                    onDismissRequest = { humExpanded = false }
                ) {
                    humidities.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = "$item%") },
                            onClick = {
                                humidityText = item
                                humExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val day = dayText.toIntOrNull()
                val temp = tempText.toDoubleOrNull()
                val hum = humidityText.toDoubleOrNull()

                if (breed.isEmpty() || day == null || temp == null || hum == null) {
                    Toast.makeText(context, "Please enter valid data", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                stage = getInstarStage(day)
                val adviceResult = getClimateAdvice(stage, temp, hum)
                result = adviceResult

                // Save to DB
                val currentTime = System.currentTimeMillis()
                val calendar = Calendar.getInstance()
                val month = calendar.get(Calendar.MONTH)
                val season = when(month) {
                    in 2..5 -> "Summer"
                    in 6..9 -> "Monsoon"
                    else -> "Winter"
                }
                val weatherCondition = if (temp > 30) "Sunny" else if (temp < 22) "Cold" else "Normal"

                val entry = ClimateEntry(
                    userEmail = userEmail,
                    breed = breed,
                    day = day,
                    stage = stage,
                    temperature = temp,
                    humidity = hum,
                    status = adviceResult.status,
                    advice = adviceResult.advice,
                    timestamp = currentTime,
                    weatherCondition = weatherCondition,
                    season = season
                )

                scope.launch {
                    withContext(Dispatchers.IO) {
                        db.climateDao().insertEntry(entry)

                        // Fast one-shot analysis in background
                        val recentEntries = db.climateDao().getRecentEntries(userEmail)
                        val recommendation = RecommendationEngine().analyze(userEmail, breed, recentEntries)
                        db.recommendationDao().insertRecommendation(recommendation)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Analyze & Save", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        if (result != null) {
            Spacer(modifier = Modifier.height(32.dp))
            ResultCard(result!!, stage)
        }
    }
}
}

@Composable
fun ResultCard(result: ClimateResult, stage: String) {
    val (bgColor, accentColor) = when (result.status) {
        "SAFE" -> SuccessGreen.copy(alpha = 0.1f) to SuccessGreen
        "MODERATE" -> WarningOrange.copy(alpha = 0.1f) to WarningOrange
        "DANGER" -> DangerRed.copy(alpha = 0.1f) to DangerRed
        else -> Color.Gray.copy(alpha = 0.1f) to Color.Gray
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(accentColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = result.status,
                    style = MaterialTheme.typography.titleLarge,
                    color = accentColor,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(text = "Current Stage: $stage", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = result.advice, style = MaterialTheme.typography.bodyLarge)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Advice updated just now", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            }
        }
    }
}

private fun getInstarStage(day: Int): String {
    return when (day) {
        in 1..3 -> "1st Instar"
        in 4..6 -> "2nd Instar"
        in 7..10 -> "3rd Instar"
        in 11..15 -> "4th Instar"
        in 16..25 -> "5th Instar"
        in 26..100 -> "Harvest Stage"
        else -> "Invalid Day"
    }
}

private fun getClimateAdvice(stage: String, temp: Double, humidity: Double): ClimateResult {
    val idealTempRange: ClosedFloatingPointRange<Double>
    val idealHumRange: ClosedFloatingPointRange<Double>

    when (stage) {
        "1st Instar", "2nd Instar" -> { 
            idealTempRange = 26.0..28.0 
            idealHumRange = 85.0..90.0 
        }
        "3rd Instar" -> { 
            idealTempRange = 25.0..27.0 
            idealHumRange = 80.0..85.0 
        }
        "4th Instar", "5th Instar" -> { 
            idealTempRange = 24.0..26.0 
            idealHumRange = 70.0..75.0 
        }
        else -> { 
            idealTempRange = 23.0..25.0 
            idealHumRange = 65.0..70.0 
        }
    }

    return if (temp in idealTempRange && humidity in idealHumRange) {
        ClimateResult("SAFE", "Conditions are perfect for $stage. Keep it up!")
    } else if (temp < idealTempRange.start - 2 || temp > idealTempRange.endInclusive + 2 ||
               humidity < idealHumRange.start - 5 || humidity > idealHumRange.endInclusive + 5) {
        ClimateResult("DANGER", "Extreme conditions! Adjust environment immediately to save the batch.")
    } else {
        ClimateResult("MODERATE", "Conditions are slightly off. Check ventilation and maintain stable temp/humidity.")
    }
}
