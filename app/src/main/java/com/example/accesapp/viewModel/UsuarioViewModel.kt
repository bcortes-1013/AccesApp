package com.example.accesapp.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.accesapp.data.UsuarioRepository
import com.example.accesapp.model.User
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UsuarioViewModel: ViewModel() {

    private val users = mutableStateListOf<User>()

    suspend fun login(nombreUsuario: String, contrasena: String): User? {
        val usuario = UsuarioRepository.login(nombreUsuario, contrasena)
        usuario?.let { if (!users.contains(it)) users.add(it) }
        return usuario
    }

    fun agregarUsuario(
        rut: String,
        nombreUsuario: String,
        nombre: String,
        apellidoP: String,
        apellidoM: String,
        correo: String,
        contrasena: String,
        genero: String
    ) {
        val user = User(
            rut = rut,
            nombreUsuario = nombreUsuario,
            nombre = nombre,
            apellidoP = apellidoP,
            apellidoM = apellidoM,
            correo = correo,
            contrasena = contrasena,
            genero = genero
        )
        viewModelScope.launch {
            UsuarioRepository.agregar(user)
        }
    }

    suspend fun obtenerUsuarios() {
        users.clear()
        users.addAll(UsuarioRepository.obtenerUsuarios())
    }

    suspend fun recuperar(correo: String): String? {
        return UsuarioRepository.recuperarContrasena(correo)
    }

    fun esRutValido(rut: String): Boolean {
        // Limpiar puntos y espacios
        val cleanRut = rut.replace(".", "").replace(" ", "").uppercase()

        // Validar formato
        if (!cleanRut.matches(Regex("^\\d{7,8}-[0-9K]$"))) return false

        val (numero, dv) = cleanRut.split("-")
        return calcularDv(numero) == dv
    }

    // Función que calcula el dígito verificador
    fun calcularDv(rut: String): String {
        var suma = 0
        var factor = 2
        for (i in rut.reversed()) {
            suma += (i.toString().toInt()) * factor
            factor = if (factor < 7) factor + 1 else 2
        }
        val dv = 11 - (suma % 11)
        return when (dv) {
            11 -> "0"
            10 -> "K"
            else -> dv.toString()
        }
    }

    fun debugUsuarios() {
        println("🔎 Lista de usuarios almacenados:")
        users.forEach {
            println(" - ${it.nombreUsuario} (${it.correo})")
        }
    }
}