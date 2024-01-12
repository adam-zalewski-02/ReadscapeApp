package com.example.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.search.CameraRoute
import com.example.search.SearchRoute

const val searchRoute = "search_route"
const val cameraRoute = "camera_route"
fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
    this.navigate(searchRoute, navOptions)
}

fun NavController.navigateToCamera(navOptions: NavOptions? = null) {
    this.navigate(cameraRoute, navOptions)
}

fun NavGraphBuilder.searchScreen(
    onBackClick: () -> Unit,
    onBookClick: (String) -> Unit,
    onScanClick: () -> Unit,
) {
    composable(route = searchRoute) {
        SearchRoute(
            onBackClick = onBackClick,
            onBookClick = onBookClick,
            onScanClick = onScanClick,
        )
    }
}

fun NavGraphBuilder.cameraScreen(
    onBackClick: () -> Unit,
) {
    composable(route = cameraRoute) {
        CameraRoute(onBackClick)
    }
}