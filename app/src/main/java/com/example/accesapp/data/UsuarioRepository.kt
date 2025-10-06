package com.example.accesapp.data

import androidx.compose.runtime.mutableStateListOf
import com.example.accesapp.model.Credenciales
import com.example.accesapp.model.User

object UsuarioRepository {
    private val users = mutableStateListOf<User>()

    init {
        // Usuario de prueba estático
        users.add(
            User(
                rut = "11111111-1",
                nombreUsuario = "admin",
                nombre = "Administrador",
                apellidoP = "Prueba",
                apellidoM = "Demo",
                correo = "admin@demo.com",
                contrasena = "1234",
                genero = "masculino"
            )
        )
        users.add(
            User(
                rut = "12333444-5",
                nombreUsuario = "camila_s",
                nombre = "Camila",
                apellidoP = "Serrano",
                apellidoM = "Ortiz",
                correo = "camila.serrano@example.com",
                contrasena = "camila789",
                genero = "femenino"
            )
        )
        users.add(
            User(
                rut = "13444555-6",
                nombreUsuario = "santiago_r",
                nombre = "Santiago",
                apellidoP = "Rojas",
                apellidoM = "Vargas",
                correo = "santiago.rojas@example.com",
                contrasena = "santi2025",
                genero = "masculino"
            )
        )
        users.add(
            User(
                rut = "14555666-7",
                nombreUsuario = "valentina_m",
                nombre = "Valentina",
                apellidoP = "Martínez",
                apellidoM = "Fuentes",
                correo = "valentina.martinez@example.com",
                contrasena = "valen123",
                genero = "femenino"
            )
        )
        users.add(
            User(
                rut = "15666777-8",
                nombreUsuario = "diego_c",
                nombre = "Diego",
                apellidoP = "Castro",
                apellidoM = "Luna",
                correo = "diego.castro@example.com",
                contrasena = "diego456",
                genero = "masculino"
            )
        )
        users.add(
            User(
                rut = "16777888-9",
                nombreUsuario = "josefina_p",
                nombre = "Josefina",
                apellidoP = "Paredes",
                apellidoM = "Muñoz",
                correo = "josefina.paredes@example.com",
                contrasena = "jose2025",
                genero = "femenino"
            )
        )
    }


    suspend fun login(nombreUsuario: String, contrasena: String): User? {
        return try {
            val cred = Credenciales(nombreUsuario, contrasena)
            val response = ApiClient.apiService.login(cred)
            println(cred)
            println(response)
            if (response.isSuccessful) {
                response.body()?.also { agregar(it) } // guarda localmente también
            } else {
                users.find { it.nombreUsuario.equals(nombreUsuario, true) && it.contrasena == contrasena }
            }
        } catch (e: Exception) {
            users.find { it.nombreUsuario.equals(nombreUsuario, true) && it.contrasena == contrasena }
        }
    }

    // Obtener todos los usuarios
    suspend fun obtenerUsuarios(): List<User> {
        return try {
            val response = ApiClient.apiService.getUsuarios()
            if (response.isSuccessful) response.body() ?: users.toList()
            else users.toList()
        } catch (e: Exception) {
            users.toList()
        }
    }

    // Agregar usuario
    suspend fun agregar(user: User) {
        try {
            val response = ApiClient.apiService.registrarUsuario(user)
            println(response)
            if (response.isSuccessful) {
                response.body()?.let { users.add(it) }
            } else {
                users.add(user) // fallback local
            }
        } catch (e: Exception) {
            users.add(user) // fallback si falla la API
        }
    }

    suspend fun recuperarContrasena(correo: String): String? {
        return try {
            val response = ApiClient.apiService.recuperarContrasena(correo)
            if (response.isSuccessful) {
                response.body()?.contrasena
            } else {
                users.find { it.correo.equals(correo, ignoreCase = true) }?.contrasena
            }
        } catch (e: Exception) {
            users.find { it.correo.equals(correo, ignoreCase = true) }?.contrasena
        }
    }
}