package com.example.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.database.dao.DarkThemeConfigDao
import com.example.database.dao.RecentSearchQueryDao
import com.example.database.dao.ThemeBrandDao
import com.example.database.dao.UserDao
import com.example.database.dao.UserDataDao
import com.example.database.model.DarkThemeConfigEntity
import com.example.database.model.RecentSearchQueryEntity
import com.example.database.model.ThemeBrandEntity
import com.example.database.model.UserDataEntity
import com.example.database.model.UserEntity
import com.example.database.util.DarkThemeConfigConverter
import com.example.database.util.InstantConverter
import com.example.database.util.SetStringConverter
import com.example.database.util.ThemeBrandConverter

@Database(
    entities = [
        UserEntity::class,
        RecentSearchQueryEntity::class,
        UserDataEntity::class,
        DarkThemeConfigEntity::class,
        ThemeBrandEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    InstantConverter::class,
    DarkThemeConfigConverter::class,
    ThemeBrandConverter::class,
    SetStringConverter::class,
)
abstract class BookDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun recentSearchQueryDao(): RecentSearchQueryDao
    abstract fun darkThemeConfigDao() : DarkThemeConfigDao
    abstract fun themeBrandDao() : ThemeBrandDao
    abstract fun userDataDao() : UserDataDao
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