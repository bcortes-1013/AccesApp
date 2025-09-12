package com.example.accesapp.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "sesion_prefs")

object SesionManager {
    private val KEY_USUARIO_ACTIVO = stringPreferencesKey("usuario_activo")

    suspend fun guardarUsuarioActivo(context: Context, nombreUsuario: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USUARIO_ACTIVO] = nombreUsuario
        }
    }

    fun obtenerUsuarioActivo(context: Context): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[KEY_USUARIO_ACTIVO]
        }
    }

    suspend fun cerrarSesion(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_USUARIO_ACTIVO)
        }
    }
}