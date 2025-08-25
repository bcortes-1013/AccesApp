package com.example.accesapp.views

import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.example.accesapp.model.UsuarioRepositorio
import com.example.accesapp.navigation.NavRouter
import com.example.accesapp.R
import java.util.*

@Composable
fun AnimacionLottie() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.menu_principal))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        composition = composition,
        progress = progress,
        modifier = Modifier.size(380.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navController: NavController) {
    val context = LocalContext.current
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    var nombreUsuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }

    var tts: TextToSpeech? by remember { mutableStateOf(null) }

    // Inicializar TTS
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
                        "AccesAPP",
                        color = Color(0xFFCECECE),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF272635))
            )
        },
        containerColor = Color(0xFFE8E9F3)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimacionLottie()

            Spacer(modifier = Modifier.height(16.dp))

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
                label = { Text("Contraseña", fontSize = 16.sp) },
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
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    TextButton(onClick = { passwordVisible = !passwordVisible }) {
                        Text(if (passwordVisible) "Ocultar" else "Mostrar", color = Color(0xFF272635))
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Iniciar sesión
            Button(
                onClick = {
                    val usuario = UsuarioRepositorio.buscarUsuarioPorNombreUsuario(nombreUsuario, contrasena)
                    if (usuario != null) {
                        errorMessage = null
                        navController.navigate("menu/${usuario.nombreUsuario}")
                    } else {
                        errorMessage = "Correo o contraseña incorrectos"
                        tts?.speak(errorMessage, TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF474652),
                    contentColor = Color(0xFFCECECE)
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
                    Text("Registrarse", color = Color(0xFF474652), fontSize = 16.sp)
                }
                TextButton(onClick = { navController.navigate(NavRouter.Recover.route) }) {
                    Text("Recuperar contraseña", color = Color(0xFF474652), fontSize = 16.sp)
                }
            }

            errorMessage?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = it, color = Color.Red, fontSize = 16.sp, textAlign = TextAlign.Center)
            }
        }
    }
}
