package com.example.accesapp.viewModel


import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.accesapp.model.Usuario

class UsuarioViewModel: ViewModel() {

    private val _usuarios = mutableStateListOf<Usuario>();

//    val usuarios: List<Usuario> get() = _usuarios

    init {
        // Usuario de prueba estático
        _usuarios.add(
            Usuario(
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
        _usuarios.add(
            Usuario(
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
        _usuarios.add(
            Usuario(
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
        _usuarios.add(
            Usuario(
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
        _usuarios.add(
            Usuario(
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
        _usuarios.add(
            Usuario(
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

    fun agregar(usuario: Usuario) {

        if(_usuarios.any { it.rut.equals(usuario.rut, ignoreCase = true) }) return

        _usuarios.add(usuario)

    }

    fun buscar(query: String): List<Usuario> {

        if(query.isBlank()) return _usuarios

        val q = query.trim().lowercase()

        return _usuarios.filter {
            it.rut.lowercase().contains(q) ||
                    it.nombre.lowercase().contains(q) ||
                    it.apellidoP.lowercase().contains(q) ||
                    it.apellidoM.lowercase().contains(q)
        }

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
        val nuevo = Usuario(
            rut,
            nombreUsuario,
            nombre,
            apellidoP,
            apellidoM,
            correo,
            contrasena,
            genero
        )
        _usuarios.add(nuevo)
    }

    fun obtenerUsuarios(): List<Usuario> {
        return _usuarios.toList()
    }

    fun validarLogin(nombreUsuario: String, contrasena: String): Usuario? =
        _usuarios.find { it.nombreUsuario.equals(nombreUsuario, ignoreCase = true) && it.contrasena == contrasena
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
}