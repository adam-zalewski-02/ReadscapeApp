package com.example.readscape.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.authentication.navigation.loginRoute
import com.example.authentication.navigation.navigateToLoginScreen
import com.example.readscape.navigation.TopLevelDestination


@Composable
fun rememberReadscapeAppState(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController()
): ReadscapeAppState {
    return remember(
        navController,
        WindowSizeClass
    ) {
        ReadscapeAppState(
            navController,
            windowSizeClass
        )
    }
}

@Stable
class ReadscapeAppState(
    val navController: NavHostController,
    val windowSizeClass: WindowSizeClass,

) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            loginRoute -> TopLevelDestination.LOGIN
            else -> null
        }

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    fun navigateToLoginScreen() {
        navController.navigateToLoginScreen()
    }
}