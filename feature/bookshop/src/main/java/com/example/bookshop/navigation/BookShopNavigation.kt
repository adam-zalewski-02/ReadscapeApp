package com.example.bookshop.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.bookshop.BookShopRoute

const val bookShopRoute = "book_shop_route"

fun NavController.navigateToBookShopScreen(navOptions: NavOptions? = null) {
    this.navigate(bookShopRoute, navOptions)
}

fun NavGraphBuilder.bookShopScreen() {
    composable(
        route = bookShopRoute,
    ) {
        BookShopRoute()
    }
}