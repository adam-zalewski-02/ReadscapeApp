package com.example.data.di

import com.example.data.repository.BookRepository
import com.example.data.repository.DefaultBookRepository
import com.example.data.repository.DefaultUserRepository
import com.example.data.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindsUserRepository(
        userRepository: DefaultUserRepository,
    ): UserRepository

    @Binds
    fun bindsBookRepository(
        bookRepository: DefaultBookRepository,
    ): BookRepository
}