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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.reshmenammapride.ui.theme.ReshmeTheme
import kotlinx.coroutines.launch

class SignupFragment : Fragment() {

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
                    SignupScreen(
                        db = db,
                        onSignupSuccess = { findNavController().navigate(R.id.action_signupFragment_to_loginFragment) },
                        onBackToLogin = { findNavController().navigate(R.id.action_signupFragment_to_loginFragment) },
                        context = requireContext(),
                        prefs = requireActivity().getSharedPreferences("ReshmePrefs", Context.MODE_PRIVATE)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    db: AppDatabase,
    onSignupSuccess: () -> Unit,
    onBackToLogin: () -> Unit,
    context: android.content.Context,
    prefs: android.content.SharedPreferences
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("Beginner") }
    var expanded by remember { mutableStateOf(false) }
    val experiences = listOf("Beginner", "Intermediate", "Expert")

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Join our community of smart farmers",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = village,
            onValueChange = { village = it },
            label = { Text("Village / District") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = experience,
                onValueChange = {},
                readOnly = true,
                label = { Text("Experience Level") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.Star, contentDescription = null) }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                experiences.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            experience = item
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Please fill required fields", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                scope.launch {
                    val existingUser = db.userDao().getUserByEmail(email)
                    if (existingUser != null) {
                        Toast.makeText(context, "User already exists", Toast.LENGTH_SHORT).show()
                    } else {
                        val newUser = User(email, name, password, phone, village, experience)
                        db.userDao().registerUser(newUser)
                        
                        // "Remember Me" logic: save to prefs so LoginFragment can pre-fill
                        prefs.edit().apply {
                            putString("last_signup_email", email)
                            putString("last_signup_password", password)
                            apply()
                        }
                        
                        Toast.makeText(context, "Account Created!", Toast.LENGTH_SHORT).show()
                        onSignupSuccess()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Start Monitoring", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onBackToLogin) {
            Text("Already have an account? Login", color = MaterialTheme.colorScheme.primary)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}