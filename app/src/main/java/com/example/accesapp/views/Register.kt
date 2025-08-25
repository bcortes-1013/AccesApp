package com.example.accesapp.views

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.accesapp.model.UsuarioRepositorio
import com.example.accesapp.model.Usuarios
import com.example.accesapp.navigation.NavRouter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(navController: NavController) {
    val context = LocalContext.current

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
                        "Registro de usuario",
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
                    label = { Text(label, fontSize = 16.sp) },
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
                    visualTransformation = if (label == "Contraseña" && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                    trailingIcon = if (label == "Contraseña") {
                        {
                            TextButton(onClick = { passwordVisible = !passwordVisible }) {
                                Text(if (passwordVisible) "Ocultar" else "Mostrar", color = Color(0xFF272635))
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
                            selectedColor = Color(0xFF272635),
                            unselectedColor = Color(0xFF272635)
                        )
                    )
                    Text(
                        gen,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF474652),
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
                        checkedColor = Color(0xFF272635),
                        uncheckedColor = Color(0xFF272635),
                        checkmarkColor = Color(0xFFCECECE)
                    )
                )
                Text(
                    "Acepto términos y condiciones",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF474652),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de registro
            Button(
                onClick = {
                    if (nombreUsuario.isNotBlank() &&
                        nombre.isNotBlank() &&
                        apellidoP.isNotBlank() &&
                        apellidoM.isNotBlank() &&
                        correo.isNotBlank() &&
                        contrasena.isNotBlank() &&
                        genero.isNotBlank() &&
                        aceptoTerminos
                    ) {
                        UsuarioRepositorio.agregarUsuario(
                            Usuarios(nombreUsuario, nombre, apellidoP, apellidoM, correo, contrasena, genero)
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
                    containerColor = Color(0xFF474652),
                    contentColor = Color(0xFFCECECE)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Registrarse", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { navController.popBackStack() }) {
                Text("Volver al login", color = Color(0xFF474652), fontSize = 16.sp)
            }
        }
    }
}
