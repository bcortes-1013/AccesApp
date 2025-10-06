package com.example.accesapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast

object Network {
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }

    // ✅ Función helper para validar internet y mostrar Toast
    fun requireInternet(context: Context): Boolean {
        return if (!isInternetAvailable(context)) {
            Toast.makeText(context, "No hay conexión a Internet", Toast.LENGTH_SHORT).show()
            false
        } else true
    }
}