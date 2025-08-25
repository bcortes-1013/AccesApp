package com.example.accesapp.views

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.accesapp.model.UsuarioRepositorio
import com.example.accesapp.navigation.NavRouter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Recover(navController: NavController) {
    val context = LocalContext.current
    var correo by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf<String?>(null) }

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
                        "Recuperar contraseña",
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
            Text(
                "Ingresa tu correo para recuperar tu contraseña",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF474652),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo electrónico", fontSize = 16.sp) },
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

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val usuario = UsuarioRepositorio.obtenerUsuarios()
                        .find { it.correo.equals(correo.trim(), ignoreCase = true) }

                    resultado = if (usuario != null) {
                        "Tu contraseña es: ${usuario.contrasena}"
                    } else {
                        "El correo ingresado no está registrado."
                    }

                    tts?.speak(resultado!!, TextToSpeech.QUEUE_FLUSH, null, null)
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
                Text("Recuperar contraseña", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { navController.popBackStack() }) {
                Text("Volver al login", color = Color(0xFF474652), fontSize = 16.sp)
            }

            resultado?.let {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    it,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (it.startsWith("✅")) Color(0xFF388E3C) else Color(0xFFD32F2F),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            }
        }
    }
}
