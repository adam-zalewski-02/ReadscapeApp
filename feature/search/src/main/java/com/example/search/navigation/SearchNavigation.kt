package com.example.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.search.CameraRoute
import com.example.search.SearchRoute
import okio.Utf8
import java.net.URLDecoder
import kotlin.text.Charsets.UTF_8

const val searchRoute = "search_route/{isbn}"
const val cameraRoute = "camera_route"
fun NavController.navigateToSearch(isbn: String,navOptions: NavOptions? = null) {
    this.navigate("search_route/${isbn}", navOptions)
}

fun NavController.navigateToCamera(navOptions: NavOptions? = null) {
    this.navigate(cameraRoute, navOptions)
}

fun NavGraphBuilder.searchScreen(
    onBackClick: () -> Unit,
    onBookClick: (String) -> Unit,
    onScanClick: () -> Unit,
) {
    composable(
        route = searchRoute,
        arguments = listOf(navArgument("isbn") {type = NavType.StringType})
    ) {navBackStackEntry ->
        val arguments = requireNotNull(navBackStackEntry.arguments)
        val isbn = requireNotNull(arguments.getString("isbn"))
        val decodedIsbn = URLDecoder.decode(isbn, UTF_8.name())
        SearchRoute(
            onBackClick = onBackClick,
            onBookClick = onBookClick,
            onScanClick = onScanClick,
            isbn = decodedIsbn
        )
    }
}

fun NavGraphBuilder.cameraScreen(
    onBackClick: () -> Unit,
    onBarcodeScan: (String) -> Unit,
) {
    composable(route = cameraRoute) {
        CameraRoute(onBackClick, onBarcodeScan)
    }
}