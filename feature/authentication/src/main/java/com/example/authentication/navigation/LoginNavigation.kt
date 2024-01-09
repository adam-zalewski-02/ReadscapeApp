package com.example.authentication.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.authentication.LoginRoute
import com.example.authentication.RegisterRoute

const val loginRoute = "login_route"
const val registerRoute = "register_route"
fun NavController.navigateToLoginScreen(navOptions: NavOptions? = null) {
    this.navigate(loginRoute, navOptions)
}

fun NavController.navigateToRegisterScreen(navOptions: NavOptions? = null) {
    this.navigate(registerRoute, navOptions)
}

fun NavGraphBuilder.loginScreen(onRegisterClick: () -> Unit, onLoginClick: () -> Unit) {
    composable(
        route = loginRoute,
    ) {
        LoginRoute(onRegisterClick, onLoginClick)
    }
}

fun NavGraphBuilder.registerScreen(onBackClick: () -> Unit, onRegisterClicked: () -> Unit) {
    composable(
        route = registerRoute,
    ) {
        RegisterRoute(onBackClick, onRegisterClicked)
    }
}

