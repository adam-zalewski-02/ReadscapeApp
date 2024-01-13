package com.example.readscape.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.authentication.navigation.loginScreen
import com.example.authentication.navigation.navigateToLoginScreen
import com.example.authentication.navigation.navigateToRegisterScreen
import com.example.authentication.navigation.registerScreen
import com.example.bookdetail.navigation.bookDetailScreen
import com.example.bookdetail.navigation.navigateToBookDetailScreen
import com.example.booklistings.navigation.bookListingsScreen
import com.example.bookshop.navigation.bookShopRoute
import com.example.bookshop.navigation.bookShopScreen
import com.example.bookshop.navigation.navigateToBookShopScreen
import com.example.catalog.navigation.catalogScreen
import com.example.catalog.navigation.navigateToCatalogScreen
import com.example.readscape.ui.ReadscapeAppState
import com.example.search.navigation.cameraScreen
import com.example.search.navigation.navigateToCamera
import com.example.search.navigation.navigateToSearch
import com.example.search.navigation.searchScreen

@Composable
fun ReadscapeNavHost(
    appState: ReadscapeAppState,
    modifier: Modifier = Modifier,
    startDestination: String = bookShopRoute
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        loginScreen(
            onRegisterClick = navController::navigateToRegisterScreen,
            onLoginClick = navController::navigateToCatalogScreen,
        )
        registerScreen(onBackClick = navController::popBackStack, onRegisterClicked = navController::navigateToLoginScreen)
        bookShopScreen(onBookClick = navController::navigateToBookDetailScreen)
        bookListingsScreen()
        catalogScreen(onBookClick = navController::navigateToBookDetailScreen)
        bookDetailScreen(onBack = navController::popBackStack)
        searchScreen(
            onBackClick = navController::popBackStack,
            onBookClick = navController::navigateToBookDetailScreen,
            onScanClick = navController::navigateToCamera
        )
        cameraScreen(onBackClick = navController::popBackStack, onBarcodeScan = navController::navigateToSearch)

    }
}