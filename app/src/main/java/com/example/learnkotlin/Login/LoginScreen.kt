package com.example.learnkotlin

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learnkotlin.Login.AuthViewModel
import com.example.learnkotlin.Page.normal
import com.example.learnkotlin.Page.title
import com.example.learnkotlin.ui.theme.Primary
import com.example.learnkotlin.ui.theme.Text
import com.example.learnkotlin.ui.theme.White
import com.example.learnkotlin.R

// 1. Update the function to accept the new navigation callbacks
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val ctx = LocalContext.current

    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val loading by authViewModel.loading.collectAsState()
    val error by authViewModel.error.collectAsState()

    // Navigate when login state becomes true
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) onLoginSuccess()
    }

    // Show errors as Toasts
    LaunchedEffect(error) {
        error?.let { Toast.makeText(ctx, it, Toast.LENGTH_SHORT).show() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.a1),
            contentDescription = "Logo",
            modifier = Modifier.size(150.dp)
        )
        Text(text = "Enter your login details to", color = Text)
        Text(text = "access your account", color = Text)
        Spacer(modifier = Modifier.height(42.dp))

        Column(modifier = Modifier.padding(horizontal = 32.dp)) {
            Text(
                text = "Email", modifier = Modifier.align(Alignment.Start),
                color = Text,
                fontSize = 16.sp
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "") },
                shape = RoundedCornerShape(15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Text,
                    unfocusedTextColor = Text,
                    cursorColor = Text,
                    focusedBorderColor = Text,
                    unfocusedBorderColor = Text
                ),
                singleLine = true
            )
        }
        Spacer(modifier = Modifier.height(7.dp))
        Column(modifier = Modifier.padding(horizontal = 32.dp)) {
            Text(
                text = "Password", modifier = Modifier.align(Alignment.Start),
                color = Text,
                fontSize = 16.sp
            )
            OutlinedTextField(
                placeholder = {Text("Email")},
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "") },
                shape = RoundedCornerShape(15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Text,
                    unfocusedTextColor = Text,
                    cursorColor = Text,
                    focusedBorderColor = Text,
                    unfocusedBorderColor = Text
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(7.dp))

            // 2. Use the onForgotPasswordClicked callback
            Text(
                text = "Forgot Password?",
                color = Text,
                fontSize = 16.sp,
                modifier = Modifier.clickable { onForgotPasswordClicked() }
            )
        }
        Spacer(modifier = Modifier.height(42.dp))

        Button(
            onClick = {
                authViewModel.login(email, password)
            },
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text(
                modifier = Modifier.padding(vertical = 10.dp),
                text = "Login",
                color = Primary,

            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text(
                text = "Don't have an account? ",
                color = Text,
                fontSize = 14.sp
            )
            // 3. Use the onSignUpClicked callback
            Text(
                text = "Sign Up",
                color = White,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onSignUpClicked() },
                textDecoration = TextDecoration.Underline
            )
        }
    }
}

