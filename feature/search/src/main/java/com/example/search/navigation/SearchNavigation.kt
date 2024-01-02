package com.example.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.search.SearchRoute

const val searchRoute = "search_route"
fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
    this.navigate(searchRoute, navOptions)
}

fun NavGraphBuilder.searchScreen(
    onBackClick: () -> Unit,
    onBookClick: (String) -> Unit,

) {
    composable(route = searchRoute) {
        SearchRoute(
            onBackClick = onBackClick,
            onBookClick = onBookClick
        )
    }
}