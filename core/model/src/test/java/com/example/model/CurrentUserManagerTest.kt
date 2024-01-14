package com.example.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CurrentUserManagerTest {

    @Before
    fun setUp() {
        // Ensure that the CurrentUserManager is in a clean state before each test
        CurrentUserManager.clearCurrentUser()
    }

    @Test
    fun `setCurrentUser should set the current user`() {
        // Given a user ID
        val userId = "123"

        // When setting the current user
        CurrentUserManager.setCurrentUser(userId)

        // Then the current user should be set
        val currentUser = CurrentUserManager.getCurrentUser()
        assertEquals(userId, currentUser?.userId)
    }

    @Test
    fun `getCurrentUser should return null when no user is set`() {
        // When getting the current user without setting one
        val currentUser = CurrentUserManager.getCurrentUser()

        // Then the current user should be null
        assertNull(currentUser)
    }

    @Test
    fun `clearCurrentUser should clear the current user`() {
        // Given a user ID
        val userId = "123"
        CurrentUserManager.setCurrentUser(userId)

        // When clearing the current user
        CurrentUserManager.clearCurrentUser()

        // Then the current user should be null
        val currentUser = CurrentUserManager.getCurrentUser()
        assertNull(currentUser)
    }
}