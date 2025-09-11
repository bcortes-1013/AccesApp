package com.example.accesapp.ui.view

import android.content.Intent
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.accesapp.navigation.NavRouter
import com.example.accesapp.viewModel.ThemeViewModel
import com.example.accesapp.viewModel.UsuarioViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Speech(navController: NavController, themeViewModel: ThemeViewModel, usuarioViewModel: UsuarioViewModel) {
    val context = LocalContext.current
    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    var texto by remember { mutableStateOf("") }
    var frases by remember { mutableStateOf(
        listOf(
            "¿Me puedes ayudar, por favor?",
            "¿Dónde está la parada de bus?",
            "Gracias por tu ayuda",
            "Necesito cruzar la calle",
            "¿Me puedes indicar qué número de bus viene?"
        )
    ) }

    val clipboardManager = LocalClipboardManager.current

    // Inicializar TTS
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale("es", "ES")
//                tts?.speak("Bienvenido, $nombreUsuario", TextToSpeech.QUEUE_FLUSH, null, null)
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
                        fontSize = 20.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF272635)
                )
            )
        },
        containerColor = Color(0xFFE8E9F3)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Campo de texto accesible
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                label = { Text("Escribe algo para escuchar", fontSize = 16.sp) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFCECECE),
                    unfocusedContainerColor = Color(0xFFCECECE),
                    focusedTextColor = Color(0xFF474652),
                    unfocusedTextColor = Color(0xFF474652),
                    focusedIndicatorColor = Color(0xFF272635),
                    unfocusedIndicatorColor = Color(0xFF272635)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Acciones para el input
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        if (texto.isNotBlank()) {
                            frases = frases + texto
                            texto = ""
                        }
                    },
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF474652),
                        contentColor = Color.White
                    )
                ) {
                    Text("➕ Guardar", fontSize = 16.sp)
                }

                Button(
                    onClick = {
                        if (texto.isNotBlank()) {
                            clipboardManager.setText(AnnotatedString(texto))
                            tts?.speak("Texto copiado al portapapeles", TextToSpeech.QUEUE_FLUSH, null, null)
                        }
                    },
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF67666F), // destacado
                        contentColor = Color.White
                    )
                ) {
                    Text("📋 Copiar", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón hablar del input
            Button(
                onClick = {
                    if (texto.isNotBlank()) {
                        tts?.speak(texto, TextToSpeech.QUEUE_FLUSH, null, null)
                    } else {
                        tts?.speak("Por favor escribe un texto primero", TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF474652),
                    contentColor = Color(0xFFCECECE)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("🔊 Leer en voz alta", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Frases rápidas
            Text("Frases rápidas", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, fontSize = 20.sp)

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                frases.forEach { frase ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Botón principal (leer frase)
                        Button(
                            onClick = { tts?.speak(frase, TextToSpeech.QUEUE_FLUSH, null, null) },
                            modifier = Modifier.weight(1f).height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF474652),
                                contentColor = Color(0xFFCECECE)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(frase, textAlign = TextAlign.Center, fontSize = 16.sp)
                        }

                        // Copiar
                        Button(
                            onClick = { clipboardManager.setText(AnnotatedString(frase)) },
                            modifier = Modifier.size(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFA6A6A8), // destacado
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("📋", fontSize = 18.sp)
                            }
                        }

                        // Compartir
                        Button(
                            onClick = {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, frase)
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, null)
                                context.startActivity(shareIntent)
                            },
                            modifier = Modifier.size(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFCECECE),
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(0.dp) // importante para eliminar padding interno
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("📤", fontSize = 18.sp)
                            }
                        }

                        // Eliminar
                        Button(
                            onClick = { frases = frases - frase },
                            modifier = Modifier.size(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFF7272),
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(0.dp) // elimina padding interno
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("❌", fontSize = 18.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
