package com.example.reshmenammapride

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.FilterDrama
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.reshmenammapride.ui.theme.ReshmeTheme

class SeasonsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ReshmeTheme {
                    SeasonsScreen(onBack = { findNavController().navigateUp() })
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SeasonsScreen(onBack: () -> Unit) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Rearing Seasons", fontWeight = FontWeight.Bold) },
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Text(
                    text = "Guidelines",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Best practices for each time of the year",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )

            Spacer(modifier = Modifier.height(24.dp))

            SeasonCard(
                season = "Summer (Feb - May)",
                icon = Icons.Default.WbSunny,
                color = Color(0xFFFF9800),
                tips = listOf(
                    "Best for Bivoltine if temperature is controlled.",
                    "Ensure adequate ventilation to remove heat.",
                    "Keep leaves fresh by storing in wet bags.",
                    "Maintain 70-80% humidity in the rearing room."
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            SeasonCard(
                season = "Monsoon (June - Sept)",
                icon = Icons.Default.FilterDrama,
                color = Color(0xFF1976D2),
                tips = listOf(
                    "High risk of Grasserie and Flacherie diseases.",
                    "Avoid feeding wet mulberry leaves.",
                    "Use lime powder to reduce room humidity.",
                    "Increase frequency of bed cleaning."
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            SeasonCard(
                season = "Winter (Oct - Jan)",
                icon = Icons.Default.AcUnit,
                color = Color(0xFF00BCD4),
                tips = listOf(
                    "Growth period is extended due to cold.",
                    "Use room heaters to maintain 25°C.",
                    "Avoid early morning feeding if leaves are too cold.",
                    "Close windows at night to prevent cold drafts."
                )
            )
        }
    }
    }

    @Composable
    fun SeasonCard(season: String, icon: ImageVector, color: Color, tips: List<String>) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = season, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = color)
                }
                Spacer(modifier = Modifier.height(16.dp))
                tips.forEach { tip ->
                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text(text = "•", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = tip, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
