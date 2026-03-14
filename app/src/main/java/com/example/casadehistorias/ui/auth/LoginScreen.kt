package com.example.casadehistorias.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.casadehistorias.ui.theme.*

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            onLoginSuccess()
        }
    }

    fun validate(): Boolean {
        var valid = true
        emailError = null
        passwordError = null

        if (email.isBlank()) {
            emailError = "El correo es obligatorio"
            valid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Ingresa un correo válido"
            valid = false
        }

        if (password.isBlank()) {
            passwordError = "La contraseña es obligatoria"
            valid = false
        } else if (password.length < 6) {
            passwordError = "Mínimo 6 caracteres"
            valid = false
        }

        return valid
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(NocheProfunda, FondoTarjeta)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Header
            Text("🌙", fontSize = 56.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Bienvenido",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = LunaLlena
            )
            Text(
                text = "Inicia sesión para continuar",
                style = MaterialTheme.typography.bodyLarge,
                color = TextoSecundario
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                },
                label = { Text("Correo electrónico") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null, tint = AguaSagrada)
                },
                isError = emailError != null,
                supportingText = {
                    emailError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AguaSagrada,
                    unfocusedBorderColor = TextoSecundario.copy(alpha = 0.3f),
                    errorBorderColor = FloresCeremoniales,
                    focusedLabelColor = AguaSagrada,
                    unfocusedLabelColor = TextoSecundario,
                    cursorColor = AguaSagrada,
                    focusedTextColor = LunaLlena,
                    unfocusedTextColor = LunaLlena
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                },
                label = { Text("Contraseña") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = AguaSagrada)
                },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (showPassword) "Ocultar" else "Mostrar",
                            tint = TextoSecundario
                        )
                    }
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                isError = passwordError != null,
                supportingText = {
                    passwordError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AguaSagrada,
                    unfocusedBorderColor = TextoSecundario.copy(alpha = 0.3f),
                    errorBorderColor = FloresCeremoniales,
                    focusedLabelColor = AguaSagrada,
                    unfocusedLabelColor = TextoSecundario,
                    cursorColor = AguaSagrada,
                    focusedTextColor = LunaLlena,
                    unfocusedTextColor = LunaLlena
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (authState is AuthState.Error) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = FloresCeremoniales.copy(alpha = 0.15f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = (authState as AuthState.Error).message,
                        color = FloresCeremoniales,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Login button
            Button(
                onClick = {
                    if (validate()) {
                        authViewModel.signIn(email, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AguaSagrada,
                    contentColor = NocheProfunda
                ),
                enabled = authState !is AuthState.Loading
            ) {
                if (authState is AuthState.Loading) {
                    CircularProgressIndicator(
                        color = NocheProfunda,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        "Iniciar Sesión",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign-up link
            TextButton(onClick = onNavigateToSignUp) {
                Text(
                    "¿No tienes cuenta? ",
                    color = TextoSecundario
                )
                Text(
                    "Regístrate",
                    color = AguaSagrada,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
