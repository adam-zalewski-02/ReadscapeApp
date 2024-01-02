package com.example.data.repository

import com.example.database.dao.DarkThemeConfigDao
import com.example.database.dao.ThemeBrandDao
import com.example.database.dao.UserDataDao
import com.example.database.model.DarkThemeConfigEntity
import com.example.database.model.ThemeBrandEntity
import com.example.database.model.UserDataEntity
import com.example.database.model.asExternalModel
import com.example.model.CurrentUser
import com.example.model.CurrentUserManager
import com.example.model.DarkThemeConfig
import com.example.model.ThemeBrand
import com.example.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultUserDataRepository @Inject constructor(
    private val userDataDao: UserDataDao,
    private val themeBrandDao: ThemeBrandDao,
    private val darkThemeConfigDao: DarkThemeConfigDao,
) : UserDataRepository {

    private val currentUser: CurrentUser = CurrentUserManager.getCurrentUser() ?: CurrentUser("defaultUserId")
    override val userData: Flow<UserData>
        get() = userDataDao.getUserDataFlow(currentUser.userId).map { it?.asExternalModel() ?: getDefaultUserData() }

    private suspend fun getDefaultUserData(): UserData {
        // Provide your default values or logic here
        return UserData(
            followedVolumes = emptySet(),
            themeBrand = ThemeBrand.DEFAULT,
            darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
            useDynamicColor = false,
            shouldHideOnboarding = false
        )
    }

    override suspend fun setFollowedVolumesIds(followedVolumesIds: Set<String>) {
        val userDataEntity = userDataDao.getUserData(currentUser.userId) ?: getDefaultUserDataEntity()
        userDataDao.insertUserData(userDataEntity.copy(followedVolumes = followedVolumesIds))
    }

    private suspend fun getDefaultUserDataEntity(): UserDataEntity {
        // Provide your default values or logic here
        return UserDataEntity(
            id = currentUser.userId,
            followedVolumes = emptySet(),
            themeBrand = ThemeBrand.DEFAULT,
            darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
            useDynamicColor = false,
            shouldHideOnboarding = false
        )
    }

    override suspend fun setVolumeIdFollowed(followedVolumeId: String, followed: Boolean) {
        val userDataEntity = userDataDao.getUserData(currentUser.userId) ?: getDefaultUserDataEntity()
        val updatedFollowedVolumes = if (followed) {
            userDataEntity.followedVolumes + followedVolumeId
        } else {
            userDataEntity.followedVolumes - followedVolumeId
        }
        userDataDao.insertUserData(userDataEntity.copy(followedVolumes = updatedFollowedVolumes))
    }

    override suspend fun setThemeBrand(themeBrand: ThemeBrand) {
        val themeBrandEntity = ThemeBrandEntity(themeBrand = themeBrand)
        themeBrandDao.insertThemeBrand(themeBrandEntity)
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        val darkThemeConfigEntity = DarkThemeConfigEntity(darkThemeConfig = darkThemeConfig)
        darkThemeConfigDao.insertDarkThemeConfig(darkThemeConfigEntity)
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        val userDataEntity = userDataDao.getUserData(currentUser.userId) ?: getDefaultUserDataEntity()
        userDataDao.insertUserData(userDataEntity.copy(useDynamicColor = useDynamicColor))
    }

    override suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        val userDataEntity = userDataDao.getUserData(currentUser.userId) ?: getDefaultUserDataEntity()
        userDataDao.insertUserData(userDataEntity.copy(shouldHideOnboarding = shouldHideOnboarding))
    }
}