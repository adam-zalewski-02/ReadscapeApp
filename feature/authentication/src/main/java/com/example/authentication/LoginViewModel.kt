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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userDao: UserDao,
    private val userRepository: DefaultUserRepository
) : ViewModel() {
    private val _loginState = MutableStateFlow<AuthenticationUiState>(AuthenticationUiState.Loading)
    val loginState: StateFlow<AuthenticationUiState> = _loginState.asStateFlow()

    fun loginUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = userRepository.getUser(email, password)
                _loginState.value = AuthenticationUiState.Success(response.success)
            } catch (e: Exception) {
                _loginState.value = AuthenticationUiState.Error
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
    object Error : AuthenticationUiState()
    data class Success(val isLoggedIn: Boolean) : AuthenticationUiState()
}