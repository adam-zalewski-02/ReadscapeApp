package com.example.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.database.dao.UserDao
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val userDao: UserDao) : ViewModel() {
    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean>
        get() = _loginResult

    fun loginUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userDao.getUserByEmail(email)

            _loginResult.postValue(user != null && user.password == password)
        }
    }
}