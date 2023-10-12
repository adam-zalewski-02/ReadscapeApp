package com.example.readscape.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.example.readscape.ui.ReadscapeAppState

@Composable
fun ReadscapeNavHost(
    appState: ReadscapeAppState
) {
    val navController = appState.navController
    NavHost(
        navController = navController
    ) {
        
    }
}