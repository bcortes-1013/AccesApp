package com.example.accesapp.ui.view

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.example.accesapp.navigation.NavRouter
import com.example.accesapp.viewModel.ThemeViewModel
import com.example.accesapp.viewModel.UsuarioViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navController: NavController, themeViewModel: ThemeViewModel, usuarioViewModel: UsuarioViewModel) {

    var nombreUsuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Inicio",
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        navController.navigate(NavRouter.Home.route) {
                            popUpTo(NavRouter.Config.route) { inclusive = true }
                        }
                    }
                ) {
                    Text("Volver",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 20.sp
                    )
                }
            }
            // Campo Usuario
            OutlinedTextField(
                value = nombreUsuario,
                onValueChange = { nombreUsuario = it },
                label = { Text("Nombre de usuario", fontSize = 16.sp) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFCECECE),
                    unfocusedContainerColor = Color(0xFFCECECE),
                    focusedTextColor = Color(0xFF474652),
                    unfocusedTextColor = Color(0xFF474652),
                    focusedIndicatorColor = Color(0xFF272635),
                    unfocusedIndicatorColor = Color(0xFF272635),
                    focusedLabelColor = Color(0xFF272635),
                    unfocusedLabelColor = Color(0xFF474652),
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo Contraseña
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña", fontSize = 20.sp) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,            // El fondo del campo activo
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,          // El fondo del campo inactivo
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,               // Texto sobre el fondo (activo)
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,             // Texto sobre el fondo (inactivo)
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,            // Línea o borde al enfocar
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), // Línea tenue al desenfocar
                    focusedLabelColor = MaterialTheme.colorScheme.primary,                // Color del label cuando está enfocado
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), // Label tenue cuando no enfocado
//                    focusedContainerColor = Color(0xFFCECECE),
//                    unfocusedContainerColor = Color(0xFFCECECE),
//                    focusedTextColor = Color(0xFF474652),
//                    unfocusedTextColor = Color(0xFF474652),
//                    focusedIndicatorColor = Color(0xFF272635),
//                    unfocusedIndicatorColor = Color(0xFF272635),
//                    focusedLabelColor = Color(0xFF272635),
//                    unfocusedLabelColor = Color(0xFF474652),
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    TextButton(onClick = { passwordVisible = !passwordVisible }) {
                        Text(if (passwordVisible) "Ocultar" else "Mostrar", color = MaterialTheme.colorScheme.onSurface)
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val usuario = usuarioViewModel.validarLogin(nombreUsuario, contrasena)
                    if (usuario != null) {
                        errorMessage = null
                        navController.navigate("speech")
                    } else {
                        errorMessage = "Correo o contraseña incorrectos"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Iniciar sesión", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { navController.navigate(NavRouter.Register.route) }) {
                    Text("Registrarse", color = MaterialTheme.colorScheme.onBackground, fontSize = 20.sp)
                }
                TextButton(onClick = { navController.navigate(NavRouter.Recover.route) }) {
                    Text("Recuperar contraseña", color = MaterialTheme.colorScheme.onBackground, fontSize = 20.sp)
                }
            }

            errorMessage?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = it, color = Color.Red, fontSize = 16.sp, textAlign = TextAlign.Center)
            }
        }
    }
}
