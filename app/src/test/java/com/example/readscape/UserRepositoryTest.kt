package com.example.readscape

import com.example.readscape.data.datasource.local.UserDao
import com.example.readscape.data.model.User
import com.example.readscape.data.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class UserRepositoryTest {

    @Mock
    lateinit var userDao: UserDao

    @InjectMocks
    lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

    }

    @Test
    fun getAllUsersTest() {
        val userList = listOf(
            User(1, "user1@example.com", "password1"),
            User(2, "user2@example.com", "password2")
        )

        runBlocking {

            `when`(userDao.getAllUsers()).thenReturn(userList)

            val result = userRepository.getAllUsers()

            assertEquals(userList, result)
        }
    }

    @Test
    fun getUserByIdTest() {
        val user = User(1, "user1@example.com", "password1")

        runBlocking {
            `when`(userDao.getUserById(1)).thenReturn(user)

            val result = userRepository.getUserById(1)

            assertEquals(user, result)
        }
    }

    @Test
    fun insertUsersTest() {
        val users = listOf(
            User(1, "user1@example.com", "password1"),
            User(2, "user2@example.com", "password2")
        )

        runBlocking {
            doNothing().`when`(userDao).insertUsers(users.get(0),users.get(1))
            userRepository.insertUsers(users.get(0),users.get(1))
            verify(userDao).insertUsers(users.get(0),users.get(1))
        }
    }


    @Test
    fun deleteUserTest() {
        val user = User(1, "user1@example.com", "password1")

        runBlocking {
            // Use doNothing() for void methods
            doNothing().`when`(userDao).deleteUser(user)
            userRepository.deleteUser(user)
            verify(userDao).deleteUser(user)
        }
    }
}