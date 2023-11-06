package com.example.readscape.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.authentication.navigation.loginRoute
import com.example.authentication.navigation.loginScreen
import com.example.authentication.navigation.navigateToLoginScreen
import com.example.authentication.navigation.navigateToRegisterScreen
import com.example.authentication.navigation.registerScreen
import com.example.readscape.ui.ReadscapeAppState

@Composable
fun ReadscapeNavHost(
    appState: ReadscapeAppState,
    modifier: Modifier = Modifier,
    startDestination: String = loginRoute
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        loginScreen(
            onRegisterClick = navController::navigateToRegisterScreen,
            //onLoginClick = navController::navigateToHomeScreen
        )
        registerScreen(onBackClick = navController::navigateToLoginScreen)
    }
}