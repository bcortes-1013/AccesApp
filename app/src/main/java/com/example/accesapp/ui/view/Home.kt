package com.example.accesapp.ui.view

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.example.accesapp.navigation.NavRouter
import com.example.accesapp.R
import com.example.accesapp.data.SesionManager
import com.example.accesapp.viewModel.ThemeViewModel
import kotlinx.coroutines.launch
import java.util.Locale

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
fun Home(navController: NavController, themeViewModel: ThemeViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    val usuarioActivo by SesionManager.obtenerUsuarioActivo(context).collectAsState(initial = null)

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
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = {
                    navController.navigate(NavRouter.Config.route)
                    tts?.speak("Ajustes", TextToSpeech.QUEUE_FLUSH, null, null)
                }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Ajustes",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ajustes", color = MaterialTheme.colorScheme.onBackground, fontSize = 20.sp)
                }
                TextButton(onClick = {
                    if (usuarioActivo != null) {
                        scope.launch {
                            SesionManager.cerrarSesion(context)
                        }
                    } else {
                        navController.navigate(NavRouter.Login.route)
                    }
                    tts?.speak(if (usuarioActivo != null) "Cerrando sesión" else "Inicio de sesión", TextToSpeech.QUEUE_FLUSH, null, null)
                }) {
                    Icon(
                        imageVector = if (usuarioActivo != null) Icons.Default.Logout else Icons.Default.Login,
                        contentDescription = if (usuarioActivo != null) "Cerrar sesión" else "Iniciar sesión",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (usuarioActivo != null) "Cerrar sesión" else "Iniciar sesión", color = MaterialTheme.colorScheme.onBackground, fontSize = 20.sp)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido ${usuarioActivo ?: "Invitado"}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            AnimacionLottie()

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "AccesAPP",
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Una herramienta de apoyo para personas con discapacidad visual",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (usuarioActivo != null) {
                        navController.navigate("tools") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        navController.navigate(NavRouter.Register.route)
                    }
                    tts?.speak(if (usuarioActivo != null) "Yendo a herramientas" else "Yendo al registro", TextToSpeech.QUEUE_FLUSH, null, null)
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
                Text(if (usuarioActivo != null ) "Ir a herramientas" else "¡Regístrate!", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}