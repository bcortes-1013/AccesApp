package com.example.accesapp.model

data class Usuarios (
    val nombreUsuario: String,
    val nombre: String,
    val ApellidoP: String,
    val ApellidoM: String,
    val correo: String,
    val contrasena: String,
    val genero: String
)

object UsuarioRepositorio {
    val usuarioslist = mutableListOf<Usuarios>()

    init {
        // Usuario de prueba estático
        usuarioslist.add(
            Usuarios(
                nombreUsuario = "admin",
                nombre = "Administrador",
                ApellidoP = "Prueba",
                ApellidoM = "Demo",
                correo = "admin@demo.com",
                contrasena = "1234",
                genero = "masculino"
            )
        )
        usuarioslist.add(
            Usuarios(
                nombreUsuario = "camila_s",
                nombre = "Camila",
                ApellidoP = "Serrano",
                ApellidoM = "Ortiz",
                correo = "camila.serrano@example.com",
                contrasena = "camila789",
                genero = "femenino"
            )
        )
        usuarioslist.add(
            Usuarios(
                nombreUsuario = "santiago_r",
                nombre = "Santiago",
                ApellidoP = "Rojas",
                ApellidoM = "Vargas",
                correo = "santiago.rojas@example.com",
                contrasena = "santi2025",
                genero = "masculino"
            )
        )
        usuarioslist.add(
            Usuarios(
                nombreUsuario = "valentina_m",
                nombre = "Valentina",
                ApellidoP = "Martínez",
                ApellidoM = "Fuentes",
                correo = "valentina.martinez@example.com",
                contrasena = "valen123",
                genero = "femenino"
            )
        )
        usuarioslist.add(
            Usuarios(
                nombreUsuario = "diego_c",
                nombre = "Diego",
                ApellidoP = "Castro",
                ApellidoM = "Luna",
                correo = "diego.castro@example.com",
                contrasena = "diego456",
                genero = "masculino"
            )
        )
        usuarioslist.add(
            Usuarios(
                nombreUsuario = "josefina_p",
                nombre = "Josefina",
                ApellidoP = "Paredes",
                ApellidoM = "Muñoz",
                correo = "josefina.paredes@example.com",
                contrasena = "jose2025",
                genero = "femenino"
            )
        )
    }
    fun agregarUsuario(usuario: Usuarios) {
        usuarioslist.add(usuario)
    }
    fun obtenerUsuarios(): List<Usuarios> {
        return usuarioslist.toList()
    }
    fun buscarUsuarioPorNombreUsuario(nombreUsuario: String, contrasena: String): Usuarios? =
        usuarioslist.find { it.nombreUsuario.equals(nombreUsuario, ignoreCase = true) && it.contrasena == contrasena }
    fun buscarUsuarioPorCorreo(correo: String, contrasena: String): Usuarios? {
        return usuarioslist.find { it.correo == correo && it.contrasena == contrasena }
    }
}