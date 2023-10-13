package com.example.readscape.ui

import androidx.compose.runtime.Composable
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
@Composable
fun ReadscapeApp(
    windowSizeClass: WindowSizeClass,
    appState: ReadscapeAppState = rememberReadscapeAppState(
        windowSizeClass = windowSizeClass
    ),
) {

}