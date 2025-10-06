package com.example.accesapp

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.accesapp.ui.view.Login
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun elementosBasicosSeMuestran() {
        composeTestRule.setContent {
            Login(
                navController = androidx.navigation.testing.TestNavHostController(androidx.test.core.app.ApplicationProvider.getApplicationContext()),
                themeViewModel = com.example.accesapp.viewModel.ThemeViewModel(),
                usuarioViewModel = com.example.accesapp.viewModel.UsuarioViewModel()
            )
        }
        // Validar elementos visibles
        composeTestRule.onNodeWithText("Inicio de sesión").assertIsDisplayed()
        composeTestRule.onNodeWithText("Nombre de usuario").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()
        composeTestRule.onNodeWithText("Iniciar sesión").assertIsDisplayed()
    }
    @Test
    fun ingresoDeTextoEnCampos() {
        composeTestRule.setContent {
            Login(
                navController = androidx.navigation.testing.TestNavHostController(androidx.test.core.app.ApplicationProvider.getApplicationContext()),
                themeViewModel = com.example.accesapp.viewModel.ThemeViewModel(),
                usuarioViewModel = com.example.accesapp.viewModel.UsuarioViewModel()
            )
        }
        composeTestRule.onNodeWithText("Nombre de usuario").performTextInput("admin")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("1234")
        composeTestRule.onNodeWithText("Iniciar sesión").performClick()
    }
}