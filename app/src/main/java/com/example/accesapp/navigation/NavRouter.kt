package com.example.accesapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.accesapp.viewModel.UsuarioViewModel
import com.example.accesapp.ui.view.Home
import com.example.accesapp.ui.view.Login
import com.example.accesapp.ui.view.Register
import com.example.accesapp.ui.view.Recover
import com.example.accesapp.ui.view.Speech
import com.example.accesapp.ui.view.Config
import com.example.accesapp.viewModel.ThemeViewModel    

sealed class NavRouter(val route: String) {

    object Home : NavRouter("home")
    object Login : NavRouter("login")
    object Register : NavRouter("register")
    object Recover : NavRouter("recover")
    object Speech : NavRouter("speech")
    object Config : NavRouter("config")
}

@Composable
fun AppNav(themeViewModel: ThemeViewModel) {
    val navController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel()

    NavHost(navController = navController, startDestination = NavRouter.Home.route) {
        composable(NavRouter.Home.route) { Home(navController, themeViewModel) }
        composable(NavRouter.Login.route) { Login(navController, themeViewModel, usuarioViewModel) }
        composable(NavRouter.Recover.route) { Recover(navController, themeViewModel, usuarioViewModel) }
        composable(NavRouter.Register.route) { Register(navController, themeViewModel, usuarioViewModel) }
        composable(NavRouter.Config.route) { Config(navController, themeViewModel) }
        composable(NavRouter.Speech.route) { Speech(navController, themeViewModel, usuarioViewModel) }
    }
}