package com.example.readscape.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.readscape.R
import com.example.ui.icon.ReadscapeIcons
import com.example.bookshop.R as bookshopR
import com.example.catalog.R as catalogR

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
        selectedIcon = ReadscapeIcons.Bookmark,
        unselectedIcon = ReadscapeIcons.BookmarkBorder,
        iconTextId = catalogR.string.catalog,
        titleTextId = R.string.app_name
    )
}