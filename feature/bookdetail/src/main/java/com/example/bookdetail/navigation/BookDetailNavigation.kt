package com.example.bookdetail.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.bookdetail.BookDetailRoute
import com.example.model.book.Volume
import java.net.URLDecoder
import java.net.URLEncoder
import kotlin.text.Charsets.UTF_8

const val bookDetailRoute = "book_detail_route/{bookId}"

fun NavController.navigateToBookDetailScreen(volumeId: String, navOptions: NavOptions? = null) {
    this.navigate("book_detail_route/${volumeId}", navOptions)
}

fun NavGraphBuilder.bookDetailScreen(onBack: () -> Unit) {
    composable(
        route = bookDetailRoute,
        arguments = listOf(navArgument("bookId") { type = NavType.StringType })
    ) { navBackStackEntry ->
        val arguments = requireNotNull(navBackStackEntry.arguments)
        val bookId = requireNotNull(arguments.getString("bookId"))
        val decodedBookId = URLDecoder.decode(bookId, UTF_8.name())

        BookDetailRoute(volumeId = decodedBookId, onBack = onBack)
    }
}