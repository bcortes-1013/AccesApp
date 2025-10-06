package com.example.accesapp.ui.view

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
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
import com.example.accesapp.navigation.NavRouter
import com.example.accesapp.utils.Network
import com.example.accesapp.viewModel.ThemeViewModel
import com.example.accesapp.viewModel.UsuarioViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(navController: NavController, themeViewModel: ThemeViewModel, usuarioViewModel: UsuarioViewModel) {
    val context = LocalContext.current

    var rutError by remember { mutableStateOf(false) }
    var rut by remember { mutableStateOf("") }
    var nombreUsuario by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellidoP by remember { mutableStateOf("") }
    var apellidoM by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var aceptoTerminos by remember { mutableStateOf(false) }
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
                        "Registro",
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
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
                        navController.navigate(NavRouter.Login.route) {
                            popUpTo(NavRouter.Register.route) { inclusive = true }
                        }
                        tts?.speak("Inicio de sesión", TextToSpeech.QUEUE_FLUSH, null, null)
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
            OutlinedTextField(
                value = rut,
                onValueChange = {
                    rut = it
                    rutError = !usuarioViewModel.esRutValido(it) // true si es inválido
                },
                label = { Text("Rut usuario (xxxxxxxx-x)",fontSize = 20.sp) },
                isError = rutError,
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
                    errorTextColor = MaterialTheme.colorScheme.error,
                    errorIndicatorColor = MaterialTheme.colorScheme.error
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            val campos = listOf(
                "Nombre de usuario" to nombreUsuario,
                "Nombre" to nombre,
                "Apellido Paterno" to apellidoP,
                "Apellido Materno" to apellidoM,
                "Correo" to correo,
                "Contraseña" to contrasena
            )

            campos.forEach { (label, value) ->
                OutlinedTextField(
                    value = value,
                    onValueChange = { newVal ->
                        when (label) {
                            "Nombre de usuario" -> nombreUsuario = newVal
                            "Nombre" -> nombre = newVal
                            "Apellido Paterno" -> apellidoP = newVal
                            "Apellido Materno" -> apellidoM = newVal
                            "Correo" -> correo = newVal
                            "Contraseña" -> contrasena = newVal
                        }
                    },
                    label = { Text(label, fontSize = 20.sp) },
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
                    visualTransformation = if (label == "Contraseña" && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                    trailingIcon = if (label == "Contraseña") {
                        {
                            TextButton(onClick = { passwordVisible = !passwordVisible }) {
                                Text(if (passwordVisible) "Ocultar" else "Mostrar", color = MaterialTheme.colorScheme.onBackground, fontSize = 20.sp)
                            }
                        }
                    } else null
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // RadioButtons de género
            Row(verticalAlignment = Alignment.CenterVertically) {
                listOf("Masculino", "Femenino").forEach { gen ->
                    RadioButton(
                        selected = genero == gen,
                        onClick = { genero = gen },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.surface,
                            unselectedColor = MaterialTheme.colorScheme.surface
                        )
                    )
                    Text(
                        gen,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Checkbox de términos
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = aceptoTerminos,
                    onCheckedChange = { aceptoTerminos = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.surface,
                        uncheckedColor = MaterialTheme.colorScheme.surface,
                        checkmarkColor = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    "Términos y condiciones",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de registro
            Button(
                onClick = {
                    if (!Network.requireInternet(context)) return@Button
                    if (rut.isNotBlank() &&
                        nombreUsuario.isNotBlank() &&
                        nombre.isNotBlank() &&
                        apellidoP.isNotBlank() &&
                        apellidoM.isNotBlank() &&
                        correo.isNotBlank() &&
                        contrasena.isNotBlank() &&
                        genero.isNotBlank() &&
                        aceptoTerminos
                    ) {
                        usuarioViewModel.agregarUsuario(
                            rut, nombreUsuario, nombre, apellidoP, apellidoM, correo, contrasena, genero
                        )
                        val mensaje = "Usuario registrado con éxito"
                        tts?.speak(mensaje, TextToSpeech.QUEUE_FLUSH, null, null)
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                        navController.navigate("login") { popUpTo("register") { inclusive = true } }
                    } else {
                        val mensaje = "Completa todos los campos y acepta los términos"
                        tts?.speak(mensaje, TextToSpeech.QUEUE_FLUSH, null, null)
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
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
                Text("Registrarse", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}