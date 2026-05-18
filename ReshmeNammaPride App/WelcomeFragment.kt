package com.example.reshmenammapride

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.reshmenammapride.ui.theme.ReshmeTheme

class WelcomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("ReshmeDebug", "WelcomeFragment onCreateView started")
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            // Use post to allow the initial Activity draw to happen (fixes black screen)
            post {
                Log.d("ReshmeDebug", "WelcomeFragment setting Compose content (posted)")
                setContent {
                    SideEffect {
                        Log.d("ReshmeDebug", "WelcomeScreen: First frame rendered")
                    }
                    ReshmeTheme {
                        WelcomeScreen(
                            onLoginClick = { findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment) },
                            onSignupClick = { findNavController().navigate(R.id.action_welcomeFragment_to_signupFragment) }
                        )
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ReshmeDebug", "WelcomeFragment onViewCreated")
    }
}

@Composable
fun WelcomeScreen(onLoginClick: () -> Unit, onSignupClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2E7D32), // PrimaryGreen
                            Color(0xFFA5D6A7)  // PrimaryContainer
                        )
                    )
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(120.dp),
                shape = RoundedCornerShape(60.dp),
                color = Color.White
                // Removed shadowElevation to speed up rendering on emulator
            ) {
                Icon(
                    painter = androidx.compose.ui.res.painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = null,
                    modifier = Modifier.padding(30.dp).size(60.dp),
                    tint = Color(0xFF2E7D32)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Reshme-Namma Pride",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Your Digital Sericulture Guard",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(64.dp))

            Button(
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF2E7D32))
            ) {
                Text("Login", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onSignupClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Text("Create Account", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}
