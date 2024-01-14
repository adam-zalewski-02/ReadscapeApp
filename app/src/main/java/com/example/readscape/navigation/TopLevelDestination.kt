package com.example.readscape.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.readscape.R
import com.example.designsystem.icon.ReadscapeIcons
import com.example.bookshop.R as bookshopR
import com.example.catalog.R as catalogR
import com.example.booklistings.R as booklistingsR
import com.example.profile.R as profileR

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    BOOKSHOP(
        selectedIcon = ReadscapeIcons.Add,
        unselectedIcon = ReadscapeIcons.Add,
        iconTextId = bookshopR.string.bookshop,
        titleTextId = R.string.app_name
    ),
    CATALOG(
        selectedIcon = ReadscapeIcons.Bookmarks,
        unselectedIcon = ReadscapeIcons.BookmarksBorder,
        iconTextId = catalogR.string.catalog,
        titleTextId = R.string.app_name
    ),
    BOOKLISTINGS(
        selectedIcon = ReadscapeIcons.ShoppingBag,
        unselectedIcon = ReadscapeIcons.ShoppingBagBorder,
        iconTextId = booklistingsR.string.booklistings,
        titleTextId = R.string.app_name
    ),
    PROFILE(
        selectedIcon = ReadscapeIcons.Person,
        unselectedIcon = ReadscapeIcons.Person,
        iconTextId = profileR.string.profile,
        titleTextId = R.string.app_name
    ),
}