package com.example.reshmenammapride

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import androidx.room.Room
import com.example.reshmenammapride.ui.theme.ReshmeTheme

class ProfileFragment : Fragment() {

    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        db = AppDatabase.getDatabase(requireContext())

        val prefs = requireActivity().getSharedPreferences("ReshmePrefs", Context.MODE_PRIVATE)
        val userEmail = prefs.getString("user_email", "") ?: ""

        return ComposeView(requireContext()).apply {
            // Dispose of the Composition when the view's LifecycleOwner is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ReshmeTheme {
                    ProfileScreen(userEmail, db)
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(userEmail: String, db: AppDatabase) {
    var user by remember { mutableStateOf<User?>(null) }
    val entries by db.climateDao().getEntriesForUser(userEmail).collectAsState(initial = emptyList<ClimateEntry>())

    val stats = remember(entries) {
        if (entries.isNotEmpty()) {
            val safeCount = entries.count { it.status.contains("SAFE") }
            ProfileStats(
                totalChecks = entries.count(),
                successRate = (safeCount.toFloat() / entries.count() * 100).toInt()
            )
        } else {
            ProfileStats()
        }
    }

    LaunchedEffect(userEmail) {
        user = db.userDao().getUserByEmail(userEmail)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = user?.name ?: "Farmer",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = userEmail,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(label = "Total Checks", value = stats.totalChecks.toString())
            StatItem(label = "Success Rate", value = "${stats.successRate}%")
        }

        Spacer(modifier = Modifier.height(32.dp))

        InfoCard(title = "Location", content = user?.village ?: "Not Set")
        Spacer(modifier = Modifier.height(12.dp))
        InfoCard(title = "Experience", content = user?.experience ?: "Beginner")
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = { /* Logout handled by activity */ },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Logout", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
        Text(text = label, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
fun InfoCard(title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = title, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                Text(text = content, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            }
        }
    }
}

data class ProfileStats(
    val totalChecks: Int = 0,
    val successRate: Int = 0
)