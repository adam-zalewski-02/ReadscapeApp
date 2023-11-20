package com.example.authentication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

@Composable
internal fun RegisterRoute(
    onBackClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    RegisterScreen(
        onBackClick = onBackClick,
        onRegisterClick = viewModel::registerUser,
    )
}

@Composable
fun RegisterScreen(
    onBackClick: () -> Unit,
    onRegisterClick: (String, String, String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatedPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IconButton(
            onClick = { onBackClick() }
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
        }

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

        OutlinedTextField(
            value = repeatedPassword,
            onValueChange = { repeatedPassword = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            label = { Text("Repeat Password") },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            onRegisterClick(email, password, repeatedPassword)
        },
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
        ) {
            Text(text = "Register")
        }
        

    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatedPassword by remember { mutableStateOf("") }


}