package com.example.accesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.accesapp.navigation.AppNav
import com.example.accesapp.ui.theme.AccesAppTheme
import com.example.accesapp.viewModel.ThemeViewModel

class MainActivity : ComponentActivity() {
    private val themeViewModel by viewModels<ThemeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeMode = themeViewModel.themeMode.value

            AccesAppTheme(themeMode = themeMode) {
                AppNav(themeViewModel = themeViewModel)
//                Main(themeViewModel = themeViewModel)
            }
        }
    }
}