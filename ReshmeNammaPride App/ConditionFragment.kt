package com.example.reshmenammapride

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

class ConditionFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ReshmeTheme {
                    ConditionScreen(onBack = { findNavController().navigateUp() })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConditionScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Health & Warning Guide", fontWeight = FontWeight.Bold) },
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
        },
        containerColor = Color(0xFFF3FFF1)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Extreme Cases Section
            Text(
                text = "🚨 EXTREME CASE WARNINGS",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFC62828),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Immediate Action Required:",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB71C1C)
                    )
                    Text(
                        text = "• Sudden mass mortality.\n• Worms crawling out of trays in large numbers.\n• Foul smell (Rotting) from the rearing bed.\n• Temperature above 35°C or below 15°C for extended periods.",
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Poor Condition Section
            Text(
                text = "⚠️ POOR CONDITION SIGNS",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE65100),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Early Warning Symptoms:",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE65100)
                    )
                    Text(
                        text = "• Sluggish movement or loss of appetite.\n• Irregular growth (mismatched sizes in same batch).\n• Soft or fluid-filled bodies (Flacherie signs).\n• Difficulty in molting (Grasserie signs).",
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Preventive Measures
            Text(
                text = "✅ PREVENTIVE TIPS",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. Disinfection: Always wash hands before handling.\n2. Fresh Feed: Only provide clean, dry mulberry leaves.\n3. Bed Cleaning: Remove waste daily to prevent infections.\n4. Ventilation: Ensure cross-ventilation but avoid direct hot/cold drafts.",
                        lineHeight = 24.sp
                    )
                }
            }
        }
    }
}
