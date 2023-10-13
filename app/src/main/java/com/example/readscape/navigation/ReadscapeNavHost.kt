package com.example.readscape.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.authentication.navigation.loginRoute
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

    }
}