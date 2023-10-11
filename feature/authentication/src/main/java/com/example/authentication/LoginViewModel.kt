package com.example.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.database.dao.UserDao
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.database.model.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val userDao: UserDao) : ViewModel() {
    private val _loginResult = MutableLiveData<Boolean>()
    private val _registrationResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean>
        get() = _loginResult
    val registrationResult: LiveData<Boolean>
        get() = _registrationResult
    fun loginUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userDao.getUserByEmail(email)

            _loginResult.postValue(user != null && user.password == password)
        }
    }

    fun registerUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingUser = userDao.getUserByEmail(email)
            if (existingUser == null) {
                val newUser = UserEntity(0, email = email, password = password)
                _registrationResult.postValue(true)
            } else {
                _registrationResult.postValue(false)
            }
        }
    }
}