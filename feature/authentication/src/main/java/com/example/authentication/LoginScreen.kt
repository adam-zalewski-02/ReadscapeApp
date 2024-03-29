package com.example.authentication


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.database.dao.fake.FakeUserDao
import com.example.database.model.UserEntity

@Composable
internal fun LoginRoute(
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val loginState by viewModel.loginState.collectAsStateWithLifecycle()
    LoginScreen(
        onRegisterClick = onRegisterClick,
        onLoginButtonClick = viewModel::loginUser,
        onLoginClick = onLoginClick,
        modifier = modifier,
        loginState = loginState
    )
}


@Composable
internal fun LoginScreen(
    onRegisterClick: () -> Unit,
    onLoginButtonClick: (String, String) -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
    loginState: AuthenticationUiState
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            label = { Text("Email") },
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            label = { Text("Password") },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onLoginButtonClick(email, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = {
            onRegisterClick()
        },
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
            ) {
            Text(text = "Register")
        }
    }
    when(loginState) {
        is AuthenticationUiState.Loading -> {
            println("loading...")
        }
        is AuthenticationUiState.Error -> {
            println("Error...")
        }
        is AuthenticationUiState.Success -> {
            onLoginClick()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val fakeUserDao = FakeUserDao()
    fakeUserDao.insertUsers(UserEntity(1, "test@example.com", "password123"))
    //val viewModel = LoginViewModel(fakeUserDao)

}
