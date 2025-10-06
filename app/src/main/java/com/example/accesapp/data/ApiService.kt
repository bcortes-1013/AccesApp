package com.example.accesapp.data

import com.example.accesapp.model.Credenciales
import com.example.accesapp.model.User
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface ApiService {
    @POST("login")
    suspend fun login(@Body credenciales: Credenciales): Response<User>

    @POST("usuarios")
    suspend fun registrarUsuario(@Body user: User): Response<User>

    @GET("usuarios")
    suspend fun getUsuarios(): Response<List<User>>

    @GET("usuarios/recover")
    suspend fun recuperarContrasena(@Query("correo") correo: String): Response<User>
}