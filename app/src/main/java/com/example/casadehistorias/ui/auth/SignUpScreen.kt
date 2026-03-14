package com.example.casadehistorias.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }
    var community by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmError by remember { mutableStateOf<String?>(null) }
    var nameError by remember { mutableStateOf<String?>(null) }

    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            onSignUpSuccess()
        }
    }

    fun validate(): Boolean {
        var valid = true
        emailError = null; passwordError = null; confirmError = null; nameError = null

        if (displayName.isBlank()) {
            nameError = "El nombre es obligatorio"
            valid = false
        }
        if (email.isBlank()) {
            emailError = "El correo es obligatorio"
            valid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Ingresa un correo válido"
            valid = false
        }
        if (password.length < 6) {
            passwordError = "Mínimo 6 caracteres"
            valid = false
        }
        if (password != confirmPassword) {
            confirmError = "Las contraseñas no coinciden"
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
                .padding(horizontal = 28.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Back button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = onNavigateToLogin) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = LunaLlena
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Header
            Text("✨", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = LunaLlena
            )
            Text(
                text = "Únete a la comunidad de narradores",
                style = MaterialTheme.typography.bodyLarge,
                color = TextoSecundario
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Name
            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it; nameError = null },
                label = { Text("Nombre completo") },
                leadingIcon = { Icon(Icons.Default.Person, null, tint = AguaSagrada) },
                isError = nameError != null,
                supportingText = { nameError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = signUpFieldColors()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; emailError = null },
                label = { Text("Correo electrónico") },
                leadingIcon = { Icon(Icons.Default.Email, null, tint = AguaSagrada) },
                isError = emailError != null,
                supportingText = { emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = signUpFieldColors()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Community
            OutlinedTextField(
                value = community,
                onValueChange = { community = it },
                label = { Text("Comunidad de origen (opcional)") },
                leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = AguaSagrada) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = signUpFieldColors()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; passwordError = null },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = AguaSagrada) },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null,
                            tint = TextoSecundario
                        )
                    }
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                isError = passwordError != null,
                supportingText = { passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = signUpFieldColors()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Confirm password
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; confirmError = null },
                label = { Text("Confirmar contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = AguaSagrada) },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                isError = confirmError != null,
                supportingText = { confirmError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = signUpFieldColors()
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

            // Register button
            Button(
                onClick = {
                    if (validate()) {
                        authViewModel.signUp(email, password, displayName, community)
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
                        "Crear Cuenta",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = onNavigateToLogin) {
                Text("¿Ya tienes cuenta? ", color = TextoSecundario)
                Text("Inicia sesión", color = AguaSagrada, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun signUpFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = AguaSagrada,
    unfocusedBorderColor = TextoSecundario.copy(alpha = 0.3f),
    errorBorderColor = FloresCeremoniales,
    focusedLabelColor = AguaSagrada,
    unfocusedLabelColor = TextoSecundario,
    cursorColor = AguaSagrada,
    focusedTextColor = LunaLlena,
    unfocusedTextColor = LunaLlena
)
