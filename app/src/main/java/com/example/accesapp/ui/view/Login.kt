package com.example.accesapp.ui.view

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.accesapp.data.SesionManager
import com.example.accesapp.navigation.NavRouter
import com.example.accesapp.viewModel.ThemeViewModel
import com.example.accesapp.viewModel.UsuarioViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import com.example.accesapp.utils.Network

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navController: NavController, themeViewModel: ThemeViewModel, usuarioViewModel: UsuarioViewModel) {

    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var nombreUsuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale("es", "ES")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Inicio de sesión",
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
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        navController.navigate(NavRouter.Home.route) {
                            popUpTo(NavRouter.Config.route) { inclusive = true }
                        }
                        tts?.speak("Inicio", TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardDoubleArrowLeft,
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Volver",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 20.sp
                    )
                }
            }
            // Campo Usuario
            OutlinedTextField(
                value = nombreUsuario,
                onValueChange = { nombreUsuario = it },
                label = { Text("Nombre de usuario", fontSize = 20.sp) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(fontSize = 20.sp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.onSurface,
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
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
                textStyle = TextStyle(fontSize = 20.sp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.onSurface,
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    TextButton(onClick = { passwordVisible = !passwordVisible }) {
                        Text(if (passwordVisible) "Ocultar" else "Mostrar", color = MaterialTheme.colorScheme.onSurface)
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (!Network.requireInternet(context)) return@Button
                    scope.launch {
                        usuarioViewModel.debugUsuarios()
                        val usuario = usuarioViewModel.login(nombreUsuario, contrasena)
                        if (usuario != null) {
                            // Guardar sesión local
                            SesionManager.guardarUsuarioActivo(context, usuario.nombreUsuario)

                            val mensaje = "Inicio exitoso"
                            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                            tts?.speak(mensaje, TextToSpeech.QUEUE_FLUSH, null, null)

                            navController.navigate("tools")
                        } else {
                            val mensaje = "Correo o contraseña incorrectos"
                            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                            tts?.speak(mensaje, TextToSpeech.QUEUE_FLUSH, null, null)
                        }
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
                Text("Iniciar sesión", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(onClick = {
                    navController.navigate(NavRouter.Register.route)
                    tts?.speak("Registro", TextToSpeech.QUEUE_FLUSH, null, null)
                }) {
                    Text("Registrarse", color = MaterialTheme.colorScheme.onBackground, fontSize = 20.sp)
                }
                TextButton(onClick = {
                    navController.navigate(NavRouter.Recover.route)
                    tts?.speak("Recuperar contraseña", TextToSpeech.QUEUE_FLUSH, null, null)
                }) {
                    Text("Recuperar contraseña", color = MaterialTheme.colorScheme.onBackground, fontSize = 20.sp)
                }
            }
        }
    }
}
