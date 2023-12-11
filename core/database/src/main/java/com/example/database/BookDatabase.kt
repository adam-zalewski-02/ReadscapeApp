package com.example.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.database.dao.RecentSearchQueryDao
import com.example.database.dao.UserDao
import com.example.database.model.RecentSearchQueryEntity
import com.example.database.model.UserEntity
import com.example.database.util.InstantConverter

@Database(
    entities = [
        UserEntity::class,
        RecentSearchQueryEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    InstantConverter::class
)
abstract class BookDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun recentSearchQueryDao(): RecentSearchQueryDao
    companion object {
        @Volatile
        private var INSTANCE: BookDatabase? = null

        fun getInstance(context: Context): BookDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    BookDatabase::class.java,
                    "book_database"
                ).fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it}
            }
        }
    }
}