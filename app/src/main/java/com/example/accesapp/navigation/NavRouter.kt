package com.example.accesapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.accesapp.views.Home
import com.example.accesapp.views.Login
import com.example.accesapp.views.Register
import com.example.accesapp.views.Recover


sealed class NavRouter(val route: String) {
    object Login : NavRouter("login")
    object Register : NavRouter("register")
    object Recover : NavRouter("recover")
    object Home : NavRouter("menu/{nombreUsuario}") {
        fun createRoute(nombreUsuario: String) = "menu/$nombreUsuario"
    }
}

@Composable
fun AppNav() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = NavRouter.Login.route) {
        composable(
            route = NavRouter.Home.route,
            arguments = listOf(navArgument("nombreUsuario") { type = NavType.StringType })
        ) { backStackEntry ->
            val nombreUsuario = backStackEntry.arguments?.getString("nombreUsuario") ?: ""
            Home(navController = navController, nombreUsuario = nombreUsuario)
        }
        composable(NavRouter.Login.route) { Login(navController) }
        composable(NavRouter.Recover.route) { Recover(navController) }
        composable(NavRouter.Register.route) { Register(navController) }
    }
}