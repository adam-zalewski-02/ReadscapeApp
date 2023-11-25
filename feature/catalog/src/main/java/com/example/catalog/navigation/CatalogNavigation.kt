package com.example.catalog.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.catalog.CatalogRoute
import com.example.model.book.Volume

const val catalogRoute = "catalog_route"

fun NavController.navigateToCatalogScreen(navOptions: NavOptions? = null) {
    this.navigate(catalogRoute, navOptions)
}

fun NavGraphBuilder.catalogScreen(onBookClick: (String) -> Unit) {
    composable(
        route = catalogRoute,
    ) {
        CatalogRoute(onBookClick)
    }
}