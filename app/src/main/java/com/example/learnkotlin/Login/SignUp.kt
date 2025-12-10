@file:Suppress("DEPRECATION")

package com.example.learnkotlin

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
// 1. Import components for the back button and layering
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// 2. Import LocalContext for showing Toasts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnkotlin.R
import com.example.learnkotlin.ui.theme.Primary
import com.example.learnkotlin.ui.theme.Text
import com.example.learnkotlin.ui.theme.White

// 3. Update the function to accept navigation callbacks
@Composable
fun SignUp(
    onBackClicked: () -> Unit,
    onSignUpSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val context = LocalContext.current

    // 4. Use a Box to layer the back button over the main content
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Primary),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.a2),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp)
            )
            Text(text = "Enter your login details to", color = Text)
            Text(text = "access your account", color = Text)
            Spacer(modifier = Modifier.height(42.dp))

            // Email TextField
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
                    )
                )
            }
            Spacer(modifier = Modifier.height(7.dp))

            // Password TextField
            Column(modifier = Modifier.padding(horizontal = 32.dp)) {
                Text(
                    text = "Password", modifier = Modifier.align(Alignment.Start),
                    color = Text,
                    fontSize = 16.sp
                )
                OutlinedTextField(
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
                    )
                )
            }
            Spacer(modifier = Modifier.height(7.dp))

            // Confirm Password TextField
            Column(modifier = Modifier.padding(horizontal = 32.dp)) {
                Text(
                    text = "Confirm password", modifier = Modifier.align(Alignment.Start),
                    color = Text,
                    fontSize = 16.sp
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text(text = "") },
                    shape = RoundedCornerShape(15.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Text,
                        unfocusedTextColor = Text,
                        cursorColor = Text,
                        focusedBorderColor = Text,
                        unfocusedBorderColor = Text
                    )
                )
            }
            Spacer(modifier = Modifier.height(38.dp))

            // SignUp Button
            Button(
                onClick = {
                    // 5. Add validation and navigation logic
                    if (email.isNotBlank() && password.isNotBlank() && password == confirmPassword) {
                        Toast.makeText(context, "Sign Up Successful!", Toast.LENGTH_SHORT).show()
                        // This executes the navigation command from MainActivity
                        onSignUpSuccess()
                    } else if (password != confirmPassword) {
                        Toast.makeText(context, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_SHORT).show()
                    }
                },
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Text
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 10.dp),
                    text = "SignUp",
                    color = Primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Legal Notice Text (remains the same)
            val annotatedString = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Text, fontSize = 14.sp)) {
                    append("By clicking on sign up, you agree to our \n\n")
                }
                val linkStyle = SpanStyle(color = White, textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                pushStringAnnotation(tag = "service", annotation = "service_link")
                withStyle(style = linkStyle) { append("Service") }
                pop()
                withStyle(style = SpanStyle(color = Text, fontSize = 14.sp)) { append(" and ") }
                pushStringAnnotation(tag = "terms", annotation = "terms_link")
                withStyle(style = linkStyle) { append("Terms") }
                pop()
            }
            ClickableText(text = annotatedString, modifier = Modifier.padding(horizontal = 32.dp), style = androidx.compose.ui.text.TextStyle(textAlign = TextAlign.Center),
                onClick = { offset ->
                    annotatedString.getStringAnnotations(tag = "service", start = offset, end = offset).firstOrNull()?.let { /* TODO */ }
                    annotatedString.getStringAnnotations(tag = "terms", start = offset, end = offset).firstOrNull()?.let { /* TODO */ }
                }
            )

            Spacer(modifier = Modifier.height(80.dp))

            // "Already have an account?" Row
            Row {
                Text(
                    text = "Already have an account? ",
                    color = Text,
                    fontSize = 14.sp
                )
                Text(
                    text = "Sign In",
                    color = White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        // 6. Use the onBackClicked callback to navigate back
                        onBackClicked()
                    },
                    textDecoration = TextDecoration.Underline
                )
            }
        }

        // 7. Add the IconButton for the back action to the top corner
        IconButton(
            onClick = onBackClicked,
            modifier = Modifier
                .padding(start = 16.dp, top = 48.dp) // Adjust padding for status bar
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Back",
                tint = Text
            )
        }
    }
}
