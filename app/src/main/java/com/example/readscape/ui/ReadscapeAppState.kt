package com.example.readscape.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.tracing.trace
import com.example.authentication.navigation.loginRoute
import com.example.authentication.navigation.navigateToLoginScreen
import com.example.authentication.navigation.registerRoute
import com.example.bookshop.navigation.bookShopRoute
import com.example.bookshop.navigation.navigateToBookShopScreen
import com.example.catalog.navigation.catalogRoute
import com.example.catalog.navigation.navigateToCatalogScreen
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
            bookShopRoute -> TopLevelDestination.BOOKSHOP
            catalogRoute -> TopLevelDestination.CATALOG
            else -> null
        }

    val shouldShowBottomBar: Boolean
        @Composable get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact && currentDestination?.route != loginRoute && currentDestination?.route != registerRoute

    val shouldShowTopAppBar: Boolean
        @Composable get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact && currentDestination?.route == bookShopRoute
    fun navigateToLoginScreen() {
        navController.navigateToLoginScreen()
    }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        trace("Navigation: ${topLevelDestination.name}") {
            val topLevelNavOptions = navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }

            when (topLevelDestination) {
                TopLevelDestination.BOOKSHOP -> {
                    navController.popBackStack(
                        catalogRoute,
                        inclusive = true
                    )
                    navController.navigateToBookShopScreen(topLevelNavOptions)}
                TopLevelDestination.CATALOG -> {
                    navController.popBackStack(
                        catalogRoute,
                        inclusive = true
                    )
                    navController.navigateToCatalogScreen(topLevelNavOptions)
                }
            }
        }
    }
}