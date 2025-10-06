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
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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





    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }

    var speechText by remember { mutableStateOf("") }

    val recognizerIntent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
        }
    }

    val permissionToRecordAudio = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),

        ) { isGranted ->
        if (isGranted) {
            speechRecognizer.startListening(recognizerIntent)
        }
        else{
            Toast.makeText(context, "Permiso denegado para el microfono", Toast.LENGTH_SHORT).show()
        }

    }

    val recognizerListener = object : RecognitionListener{
        override fun onBeginningOfSpeech() {
            Toast.makeText(context, "Escuchando voz", Toast.LENGTH_SHORT).show()
        }

        override fun onBufferReceived(p0: ByteArray?) {
            Toast.makeText(context, "Buffer recibido", Toast.LENGTH_SHORT).show()
        }

        override fun onEndOfSpeech() {
            Toast.makeText(context, "Procesando voz", Toast.LENGTH_SHORT).show()
        }

        override fun onError(p0: Int) {
            Toast.makeText(context, "Error al reconocer la voz", Toast.LENGTH_SHORT).show()
        }

        override fun onEvent(p0: Int, p1: Bundle?) {
            Toast.makeText(context, "Evento recibido", Toast.LENGTH_SHORT).show()
        }

        override fun onPartialResults(p0: Bundle?) {
            Toast.makeText(context, "Resultado parcial recibido", Toast.LENGTH_SHORT).show()
        }

        override fun onReadyForSpeech(p0: Bundle?) {
            Toast.makeText(context, "Listo para recibir voz", Toast.LENGTH_SHORT).show()
        }

        override fun onResults(p0: Bundle?) {
            val matches = p0?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            texto = matches?.firstOrNull() ?: ""
        }

        override fun onRmsChanged(p0: Float) {
            Toast.makeText(context, "Nivel de ruido detectado", Toast.LENGTH_SHORT).show()
        }

    }

    speechRecognizer.setRecognitionListener(recognizerListener)






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
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
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

            // Campo de texto grande sincronizado
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

            Button(
                onClick = {
                    if (texto.isNotBlank()) {
                        tts?.speak(texto, TextToSpeech.QUEUE_FLUSH, null, null)
                    } else {
                        tts?.speak("Por favor escribe un texto primero", TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("📖 Leer", fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    permissionToRecordAudio.launch(Manifest.permission.RECORD_AUDIO)
                    // Cuando se complete el reconocimiento, speechText tendrá el resultado.
                    // Lo sincronizamos con el texto del campo:
                    texto = speechText
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "🗣️ Ingresar texto por voz",
                    fontSize = 22.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("📤 Compartir", fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "Otras herramientas",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(20.dp))

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
                                val shareIntent = Intent.createChooser(sendIntent, "Enviar ubicación")
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

            Spacer(modifier = Modifier.height(16.dp))


        }
    }
}