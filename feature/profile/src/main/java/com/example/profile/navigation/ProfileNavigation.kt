package com.example.profile.navigation


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.profile.ProfileScreen

const val profileRoute = "profile_route"

fun NavController.navigateToProfileScreen(navOptions: NavOptions? = null) {
    this.navigate(profileRoute, navOptions)
}

fun NavGraphBuilder.profileScreen() {
    composable(
        route = profileRoute,
    ) {
        ProfileScreen()
    }
}