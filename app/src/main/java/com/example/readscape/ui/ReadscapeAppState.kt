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

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact
}