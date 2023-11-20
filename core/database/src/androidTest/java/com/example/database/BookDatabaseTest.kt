package com.example.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.database.dao.UserDao
import com.example.database.model.UserEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class BookDatabaseTest {
    private lateinit var bookDatabase: BookDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        bookDatabase = Room.inMemoryDatabaseBuilder(context, BookDatabase::class.java).build()
        userDao = bookDatabase.userDao()
    }



    @Test
    fun testInsertAndRetrieveUser() {
        println("Running test...")
        val user = UserEntity(1, "test@example.com", "password123")
        userDao.insertUsers(user)

        val retrievedUser = userDao.getUserById(1)
        assertNotNull(retrievedUser)
        assertEquals(user.id, retrievedUser?.id)
        assertEquals(user.email, retrievedUser?.email)
        assertEquals(user.password, retrievedUser?.password)
    }
}
