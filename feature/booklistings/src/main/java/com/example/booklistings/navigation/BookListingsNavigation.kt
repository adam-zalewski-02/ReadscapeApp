package com.example.booklistings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.booklistings.BookListingsScreen

const val bookListingsRoute = "book_listings_route"

fun NavController.navigateToBookListingsScreen(navOptions: NavOptions? = null) {
    this.navigate(bookListingsRoute, navOptions)
}

fun NavGraphBuilder.bookListingsScreen() {
    composable(
        route = bookListingsRoute,
    ) {
        BookListingsScreen()
    }
}
