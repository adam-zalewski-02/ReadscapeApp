package com.example.bookshop.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.bookshop.BookShopRoute
import com.example.model.book.Volume

const val bookShopRoute = "book_shop_route"

fun NavController.navigateToBookShopScreen(navOptions: NavOptions? = null) {
    this.navigate(bookShopRoute, navOptions)
}

fun NavGraphBuilder.bookShopScreen(onBookClick: (String) -> Unit) {
    composable(
        route = bookShopRoute,
    ) {
        BookShopRoute(onBookClick)
    }
}