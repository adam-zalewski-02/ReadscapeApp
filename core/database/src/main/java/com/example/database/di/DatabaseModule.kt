package com.example.database.di

import android.content.Context
import androidx.room.Room
import com.example.database.BookDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesBookDatabase(
        @ApplicationContext context: Context,
    ) : BookDatabase = Room.databaseBuilder(
        context,
        BookDatabase::class.java,
        "book_database",
    ).build()
}