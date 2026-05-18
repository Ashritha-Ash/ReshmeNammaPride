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
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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

class WeatherFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ReshmeTheme {
                    WeatherScreen(onBack = { findNavController().navigateUp() })
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun WeatherScreen(onBack: () -> Unit) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Weather & Rearing", fontWeight = FontWeight.Bold) },
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
                    text = "How outside conditions affect your batch",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )

            Spacer(modifier = Modifier.height(24.dp))

            WeatherInfoCard(
                title = "Hot & Dry (Summer)",
                icon = Icons.Default.WbSunny,
                iconColor = Color(0xFFFF9800),
                content = "High temperatures cause leaves to wither quickly. Increase frequency of leaf feeding and use wet gunny bags on windows to maintain humidity."
            )

            Spacer(modifier = Modifier.height(16.dp))

            WeatherInfoCard(
                title = "Rainy / High Humidity",
                icon = Icons.Default.WaterDrop,
                iconColor = Color(0xFF2196F3),
                content = "Excessive humidity promotes fungal diseases. Ensure good ventilation and use bed disinfectants like Vijetha or Ankush more frequently."
            )

            Spacer(modifier = Modifier.height(16.dp))

            WeatherInfoCard(
                title = "Cold Waves",
                icon = Icons.Default.Thermostat,
                iconColor = Color(0xFF00BCD4),
                content = "Growth slows down significantly below 20°C. Use electric heaters or charcoal braziers (with proper ventilation) to keep the room warm."
            )

            Spacer(modifier = Modifier.height(16.dp))

            WeatherInfoCard(
                title = "Cloudy Days",
                icon = Icons.Default.Cloud,
                iconColor = Color(0xFF9E9E9E),
                content = "Leaf quality might drop due to less photosynthesis. Avoid overfeeding as worms might be less active."
            )
        }
    }
}

    @Composable
    fun WeatherInfoCard(title: String, icon: ImageVector, iconColor: Color, content: String) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(20.dp)) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = content, style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
                }
            }
        }
    }
}
