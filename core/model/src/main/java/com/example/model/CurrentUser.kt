package com.example.model

import android.adservices.adid.AdId

object CurrentUserManager {
    private var currentUser: CurrentUser? = null

    fun setCurrentUser(id: String) {
        currentUser = CurrentUser(id)
    }
    fun getCurrentUser(): CurrentUser? {
        return currentUser
    }
    fun clearCurrentUser() {
        currentUser = null
    }
}
class CurrentUser(val userId: String)