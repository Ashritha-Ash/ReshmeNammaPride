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

class BreedInfoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ReshmeTheme {
                    BreedInfoScreen(onBack = { findNavController().navigateUp() })
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BreedInfoScreen(onBack: () -> Unit) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Silkworm Breeds", fontWeight = FontWeight.Bold) },
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
                    .background(Color(0xFFF3FFF1))
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                BreedCard(
                    title = "Bivoltine (Double Cross)",
                    content = "Produces high-quality white silk. Requires strict temperature control (24-26°C). High yield but sensitive to diseases."
                )

                Spacer(modifier = Modifier.height(16.dp))

                BreedCard(
                    title = "Multivoltine (Pure Mysore)",
                    content = "Hardy and resistant to diseases. Produces yellowish silk. Suitable for varying tropical climates."
                )

                Spacer(modifier = Modifier.height(16.dp))

                BreedCard(
                    title = "CB (Cross Breed)",
                    content = "Most popular among Indian farmers. Hybrid of Bivoltine and Multivoltine. Balanced yield and resistance."
                )
            }
        }
    }

    @Composable
    fun BreedCard(title: String, content: String) {
        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = content)
            }
        }
    }
}
