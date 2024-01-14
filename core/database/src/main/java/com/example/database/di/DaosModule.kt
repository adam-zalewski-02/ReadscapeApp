package com.example.database.di

import com.example.database.BookDatabase
import com.example.database.dao.DarkThemeConfigDao
import com.example.database.dao.RecentSearchQueryDao
import com.example.database.dao.ThemeBrandDao
import com.example.database.dao.UserDao
import com.example.database.dao.UserDataDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesUserDao(
        database: BookDatabase,
    ) : UserDao = database.userDao()

    @Provides
    fun providesRecentSearchQueryDao(
        database: BookDatabase,
    ): RecentSearchQueryDao = database.recentSearchQueryDao()

    @Provides
    fun providesUserDataDao(
        database: BookDatabase,
    ): UserDataDao = database.userDataDao()

    @Provides
    fun providesDarkThemeConfigDao(
        database: BookDatabase,
    ): DarkThemeConfigDao = database.darkThemeConfigDao()

    @Provides
    fun providesThemeBrandDao(
        database: BookDatabase,
    ): ThemeBrandDao = database.themeBrandDao()
}