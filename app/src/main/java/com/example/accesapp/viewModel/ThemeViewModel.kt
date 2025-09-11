package com.example.accesapp.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import com.example.accesapp.ui.theme.ThemeMode

class ThemeViewModel : ViewModel() {
    private val _themeMode = mutableStateOf(ThemeMode.SYSTEM)
    val themeMode: State<ThemeMode> = _themeMode

    fun setTheme(mode: ThemeMode) {
        _themeMode.value = mode
        // Aquí podrías guardar en DataStore para persistencia
    }
}