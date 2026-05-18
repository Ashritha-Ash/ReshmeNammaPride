package com.example.reshmenammapride

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.reshmenammapride.ui.theme.ReshmeTheme
import java.text.SimpleDateFormat
import java.util.*

class HistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ReshmeTheme {
                    HistoryScreen(onBack = { findNavController().navigateUp() })
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HistoryScreen(onBack: () -> Unit) {
        val context = requireContext()
        val sharedPrefs = context.getSharedPreferences("ReshmePrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPrefs.getString("user_email", "") ?: ""
        val db = AppDatabase.getDatabase(context)

        val historyEntries by db.climateDao().getEntriesForUser(userEmail).collectAsState(initial = emptyList<ClimateEntry>())

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Monitoring History", fontWeight = FontWeight.Bold) },
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
        ) { padding ->
            if (historyEntries.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.AutoMirrored.Filled.List,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("No history entries yet", color = Color.Gray)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(Color(0xFFF5F5F5)),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(historyEntries) { entry ->
                        HistoryCard(entry)
                    }
                }
            }
        }
    }

    @Composable
    fun HistoryCard(entry: ClimateEntry) {
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val dateString = sdf.format(Date(entry.timestamp))

        val statusColor = when (entry.status.lowercase()) {
            "safe" -> Color(0xFF2E7D32)
            "moderate" -> Color(0xFFF57C00)
            "danger" -> Color(0xFFD32F2F)
            else -> Color.Gray
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = entry.stage,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    Surface(
                        color = statusColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = entry.status.uppercase(),
                            color = statusColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Text(
                    text = dateString,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    DataChip(icon = Icons.Default.Warning, label = "${entry.temperature}°C", color = Color(0xFFE64A19))
                    Spacer(modifier = Modifier.width(8.dp))
                    DataChip(icon = Icons.Default.Notifications, label = "${entry.humidity}%", color = Color(0xFF1976D2))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp).padding(top = 2.dp),
                        tint = Color(0xFFC8A951)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = entry.advice,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Breed: ${entry.breed}", style = MaterialTheme.typography.bodySmall)
                    Text("Day: ${entry.day}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }

    @Composable
    fun DataChip(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, color: Color) {
        Surface(
            color = color.copy(alpha = 0.1f),
            shape = RoundedCornerShape(24.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = color)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = color)
            }
        }
    }
}
