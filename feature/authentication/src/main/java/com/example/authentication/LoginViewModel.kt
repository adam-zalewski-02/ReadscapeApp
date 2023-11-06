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
    private val _loginState = MutableLiveData<AuthenticationUiState>()
    val loginState: LiveData<AuthenticationUiState>
        get() = _loginState

    fun loginUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loginState.postValue(AuthenticationUiState.Loading)

            val response = userRepository.getUser(email, password)

            if (response.success) {
                _loginState.postValue(AuthenticationUiState.Success(true))
                println(response)
            } else {
                _loginState.postValue(AuthenticationUiState.Error("Login failed"))
            }
        }
    }

    fun registerUser(email: String, password: String, repeatedPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (repeatedPassword == password) {
                val response = userRepository.addUser(email, password)
                println(response)
            }
        }
    }
}

sealed class AuthenticationUiState {
    object Loading : AuthenticationUiState()
    data class Success(val isLoggedIn: Boolean) : AuthenticationUiState()
    data class Error(val errorMessage: String) : AuthenticationUiState()
}