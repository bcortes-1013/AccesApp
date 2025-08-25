package com.example.accesapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.accesapp.navigation.AppNav
import com.example.accesapp.ui.theme.AccesAppTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContent {
                AccesAppTheme {
                    AppNav()
                }
            }
        } catch (e: Exception) {
            Log.e("ComposeError", "Error en MenuScreen", e)
        }
    }
}