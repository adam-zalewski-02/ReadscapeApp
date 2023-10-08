package com.example.readscape.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.readscape.data.datasource.local.UserDao
import com.example.readscape.data.model.User

@Database(entities = [User::class], version = 1)
abstract class BookDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
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