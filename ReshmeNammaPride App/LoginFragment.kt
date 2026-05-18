package com.example.reshmenammapride

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.reshmenammapride.ui.theme.ReshmeTheme
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        db = AppDatabase.getDatabase(requireContext())

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ReshmeTheme {
                    LoginScreen(
                        db = db,
                        onLoginSuccess = { findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment) },
                        onGoToSignup = { findNavController().navigate(R.id.action_loginFragment_to_signupFragment) },
                        context = requireContext(),
                        prefs = requireActivity().getSharedPreferences("ReshmePrefs", Context.MODE_PRIVATE)
                    )
                }
            }
        }
    }
}

@Composable
fun LoginScreen(
    db: AppDatabase,
    onLoginSuccess: () -> Unit,
    onGoToSignup: () -> Unit,
    context: android.content.Context,
    prefs: android.content.SharedPreferences
) {
    var email by remember { mutableStateOf(prefs.getString("last_signup_email", "") ?: "") }
    var password by remember { mutableStateOf(prefs.getString("last_signup_password", "") ?: "") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Login to your sericulture guard",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(48.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                scope.launch {
                    val user = db.userDao().getUserByEmail(email)
                    if (user != null && user.password == password) {
                        prefs.edit().apply {
                            putString("user_email", user.email)
                            putString("user_name", user.name)
                            apply()
                        }
                        onLoginSuccess()
                    } else {
                        Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Login", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onGoToSignup) {
            Text("Don't have an account? Sign Up", color = MaterialTheme.colorScheme.primary)
        }
    }
}