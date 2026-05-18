package com.example.reshmenammapride

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.reshmenammapride.ui.theme.ReshmeTheme
import java.text.SimpleDateFormat
import java.util.*

class DashboardFragment : Fragment() {

    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        db = AppDatabase.getDatabase(requireContext())

        val prefs = requireActivity().getSharedPreferences("ReshmePrefs", Context.MODE_PRIVATE)
        val userName = prefs.getString("user_name", "Farmer") ?: "Farmer"
        val userEmail = prefs.getString("user_email", "") ?: ""

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ReshmeTheme {
                    DashboardScreen(
                        userName = userName,
                        userEmail = userEmail,
                        db = db,
                        onNavigate = { id -> findNavController().navigate(id) }
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardScreen(userName: String, userEmail: String, db: AppDatabase, onNavigate: (Int) -> Unit) {
    val lastEntry by db.climateDao().getEntriesForUser(userEmail).collectAsState(initial = emptyList<ClimateEntry>())
    val currentEntry = lastEntry.firstOrNull()

    val calendar = Calendar.getInstance()
    val dateString = SimpleDateFormat("EEEE, dd MMM", Locale.getDefault()).format(calendar.time)
    val month = calendar.get(Calendar.MONTH)
    val season = when(month) {
        in 2..5 -> "Summer"
        in 6..9 -> "Monsoon"
        else -> "Winter"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Hello, $userName 👋",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "$dateString • $season",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
            IconButton(
                onClick = { onNavigate(R.id.profileFragment) },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            ) {
                Icon(Icons.Default.Person, contentDescription = "Profile", tint = MaterialTheme.colorScheme.primary)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Climate Summary Card
        ClimateSummaryCard(currentEntry)

        Spacer(modifier = Modifier.height(24.dp))

        // Active Batch Card
        ActiveBatchCard(currentEntry)

        Spacer(modifier = Modifier.height(24.dp))

        // Quick Actions Header
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        val actions = listOf(
            ActionItem("Add Climate", Icons.Default.Add, R.id.climateFragment),
            ActionItem("Weather", Icons.Default.FilterDrama, R.id.weatherFragment),
            ActionItem("Seasons", Icons.Default.WbSunny, R.id.seasonsFragment),
            ActionItem("History", Icons.Default.History, R.id.historyFragment)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.height(200.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            userScrollEnabled = false
        ) {
            items(actions) { action ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .clickable { onNavigate(action.destinationId) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(action.icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = action.title, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Knowledge Base Header
        Text(
            text = "Resources",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ResourceCard("Breeds", Icons.Default.Info, R.id.breedInfoFragment, Modifier.weight(1f), onNavigate)
            ResourceCard("Health", Icons.Default.Warning, R.id.conditionFragment, Modifier.weight(1f), onNavigate)
        }

        Spacer(modifier = Modifier.height(80.dp)) // For bottom nav spacing
    }
}

@Composable
fun ResourceCard(title: String, icon: ImageVector, destinationId: Int, modifier: Modifier, onNavigate: (Int) -> Unit) {
    Card(
        modifier = modifier
            .height(80.dp)
            .clickable { onNavigate(destinationId) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSecondaryContainer)
        }
    }
}


@Composable
fun ClimateSummaryCard(entry: ClimateEntry?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder for Circular Dial
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (entry != null) "${entry.temperature.toInt()}°" else "--",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column {
                Text(
                    text = entry?.status ?: "No Data",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (entry != null) "${entry.weatherCondition} • ${entry.humidity.toInt()}% Hum" else "Check climate today",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun ActiveBatchCard(entry: ClimateEntry?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Active Batch", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = if (entry != null) "Day ${entry.day}" else "N/A",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = entry?.stage ?: "Start a new rearing cycle",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            val progress = if (entry != null) entry.day.toFloat() / 100f else 0f
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${(progress * 100).toInt()}% towards harvest",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

data class ActionItem(val title: String, val icon: ImageVector, val destinationId: Int)