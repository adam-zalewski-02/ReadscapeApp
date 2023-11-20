package com.example.database.di

import com.example.database.BookDatabase
import com.example.database.dao.UserDao
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
}