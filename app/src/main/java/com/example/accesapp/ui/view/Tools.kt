package com.example.accesapp.ui.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.accesapp.navigation.NavRouter
import com.example.accesapp.viewModel.ThemeViewModel
import com.example.accesapp.viewModel.UsuarioViewModel
import com.google.android.gms.location.LocationServices
import java.util.*

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.core.content.ContextCompat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tools(
    navController: NavController,
    themeViewModel: ThemeViewModel,
    usuarioViewModel: UsuarioViewModel
) {
    val context = LocalContext.current
    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    var texto by remember { mutableStateOf("") }
    var isOn by remember { mutableStateOf(false) }
    val emergenciaNumero = "123456789"
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    // Lanzador para pedir permiso
    val permisoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            Toast.makeText(context, "Permiso concedido", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }

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
                        "Acceso rápido",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 22.sp,
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
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

            Spacer(modifier = Modifier.height(16.dp))

            // 🚨 Emergencia
            Button(
                onClick = {
                    try {
                        // Forma segura de crear Uri
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:$emergenciaNumero")
                        // Verificar si hay actividad que pueda manejar el intent
                        if (intent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(intent)
                        } else {
                            Toast.makeText(context, "No se puede abrir el marcador", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error al intentar llamar", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(80.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("📞 Llamar a emergencia", fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de texto grande
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                label = { Text("Escribe para escuchar o compartir", fontSize = 22.sp) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(fontSize = 20.sp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.onSurface,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    focusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botones agrupados horizontalmente
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Botón para leer en voz alta
                Button(
                    onClick = {
                        if (texto.isNotBlank()) {
                            tts?.speak(texto, TextToSpeech.QUEUE_FLUSH, null, null)
                        } else {
                            tts?.speak("Por favor escribe un texto primero", TextToSpeech.QUEUE_FLUSH, null, null)
                        }
                    },
                    modifier = Modifier.weight(1f).height(80.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Leer", fontSize = 22.sp)
                }

                // Botón para compartir
                Button(
                    onClick = {
                        if (texto.isNotBlank()) {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, texto)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, null)
                            context.startActivity(shareIntent)
                        } else {
                            tts?.speak("Por favor escribe un texto primero", TextToSpeech.QUEUE_FLUSH, null, null)
                        }
                    },
                    modifier = Modifier.weight(1f).height(80.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Compartir", fontSize = 22.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 📍 Compartir ubicación
            Button(
                onClick = {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // Solicitar permiso
                        permisoLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    } else {
                        // Permiso concedido: obtener ubicación
                        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                            if (location != null) {
                                val lat = location.latitude
                                val lon = location.longitude
                                val mensaje = "Mi ubicación actual es: https://www.google.com/maps/search/?api=1&query=$lat,$lon"

                                // Mostrar Toast
                                Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()

                                // Compartir
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, mensaje)
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, "Compartir ubicación")
                                context.startActivity(shareIntent)
                            } else {
                                Toast.makeText(context, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("🗺️ Compartir ubicación", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔦 Linterna / señal (placeholder)
            Button(
                onClick = {
                    val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
                    try {
                        // Obtiene la primera cámara trasera con flash
                        val cameraId = cameraManager.cameraIdList.firstOrNull { id ->
                            cameraManager.getCameraCharacteristics(id)
                                .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
                        }
                        cameraId?.let {
                            isOn = !isOn
                            cameraManager.setTorchMode(it, isOn) // activa o desactiva linterna
                        } ?: run {
                            Toast.makeText(context, "Linterna no disponible", Toast.LENGTH_SHORT).show()
                        }
                        tts?.speak(if (isOn) "Linterna encendida" else "Linterna apagada", TextToSpeech.QUEUE_FLUSH, null, null)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(context, "Error al acceder a la linterna", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = if (isOn) "💡 Linterna encendida" else "💡 Linterna apagada",
                    fontSize = 22.sp
                )
            }
        }
    }
}