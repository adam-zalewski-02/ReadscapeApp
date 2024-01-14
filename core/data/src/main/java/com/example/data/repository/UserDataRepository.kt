package com.example.data.repository

import com.example.model.DarkThemeConfig
import com.example.model.ThemeBrand
import com.example.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    /**
     * Stream of [UserData]
     */
    val userData: Flow<UserData>

    /**
     * Sets the user's currently followed volumes
     */
    suspend fun setFollowedVolumesIds(followedVolumesIds: Set<String>)

    /**
     * Sets the user's newly followed/unfollowed volume
     */
    suspend fun setVolumeIdFollowed(followedVolumeId: String, followed: Boolean)

    /**
     * Sets the desired theme brand.
     */
    suspend fun setThemeBrand(themeBrand: ThemeBrand)

    /**
     * Sets the desired dark theme config.
     */
    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

    /**
     * Sets the preferred dynamic color config.
     */
    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)

    /**
     * Sets whether the user has completed the onboarding process.
     */
    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean)
}