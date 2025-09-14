package com.example.accesapp.navigation

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.accesapp.viewModel.UsuarioViewModel
import com.example.accesapp.ui.view.*
import com.example.accesapp.viewModel.ThemeViewModel

sealed class NavRouter(val route: String) {

    object Home : NavRouter("home")
    object Login : NavRouter("login")
    object Register : NavRouter("register")
    object Recover : NavRouter("recover")
    object Tools : NavRouter("tools")
    object Config : NavRouter("config")
}

@Composable
fun AppNav(themeViewModel: ThemeViewModel) {
    val navController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel()

    // 👉 Estado para el escalado de fuente
    var fontScale by rememberSaveable { mutableStateOf(1f) }

    CompositionLocalProvider(
        LocalDensity provides Density(
            density = LocalDensity.current.density,
            fontScale = fontScale
        )
    ) {
        NavHost(navController = navController, startDestination = NavRouter.Home.route) {
            composable(NavRouter.Home.route) { Home(navController, themeViewModel) }
            composable(NavRouter.Login.route) { Login(navController, themeViewModel, usuarioViewModel) }
            composable(NavRouter.Recover.route) { Recover(navController, themeViewModel, usuarioViewModel) }
            composable(NavRouter.Register.route) { Register(navController, themeViewModel, usuarioViewModel) }
            composable(NavRouter.Config.route) {
                Config(
                    navController,
                    themeViewModel,
                    onIncreaseFont = { fontScale = minOf(1.3f, fontScale + 0.1f) },
                    onDecreaseFont = { fontScale = maxOf(0.9f, fontScale - 0.1f) }
                )
            }
            composable(NavRouter.Tools.route) { Tools(navController, themeViewModel, usuarioViewModel) }
        }
    }
}