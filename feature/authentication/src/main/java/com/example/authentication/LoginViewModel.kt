package com.example.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.database.dao.UserDao
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.data.repository.DefaultUserRepository
import com.example.database.model.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userDao: UserDao,
    private val userRepository: DefaultUserRepository
) : ViewModel() {
    private val _loginResult = MutableLiveData<Boolean>()
    private val _registrationResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean>
        get() = _loginResult
    val registrationResult: LiveData<Boolean>
        get() = _registrationResult
    fun loginUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            //val user = userDao.getUserByEmail(email)
            //val user = userRepository.getUser(email, password)
            //_loginResult.postValue(user != null && user.password == password)
            //println(user)
            println(userRepository.getUsers())
        }
    }

    fun registerUser(email: String, password: String, repeatedPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (repeatedPassword == password) {
                val existingUser = userDao.getUserByEmail(email)
                if (existingUser == null) {
                    val newUser = UserEntity(0, email = email, password = password)
                    userDao.insertUsers(newUser)
                    _registrationResult.postValue(true)
                    println("user added")
                } else {
                    _registrationResult.postValue(false)
                }
            }
        }
    }
}