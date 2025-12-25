package com.example.learnkotlin

import android.R.attr.onClick
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
// 1. Import Box to allow layering UI elements
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
// 2. Import Icons and IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnkotlin.R
import com.example.learnkotlin.ui.theme.Primary
import com.example.learnkotlin.ui.theme.Text

// 3. Update the function signature to accept onContinueClicked
@Composable
fun ForgetPassword(
    onBackClicked: () -> Unit,
    onContinueClicked: () -> Unit
) {

    var email by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Primary),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.new_1), contentDescription = "Logo",
                modifier = Modifier.size(150.dp)
            )
            Text(text = "Enter your login details to", color = Text)
            Text(text = "access your account", color = Text)
            Spacer(modifier = Modifier.height(42.dp))

            Column(modifier = Modifier.padding(horizontal = 32.dp))
            {
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

            Spacer(modifier = Modifier.height(38.dp))

            // 4. Update the onClick logic for the "Continue" button
            Button(
                onClick = {
                    if (email.isNotBlank()) {
                        // This executes the navigation command passed from MainActivity
                        onContinueClicked()
                    } else {
                        Toast.makeText(context, "Please enter your email.", Toast.LENGTH_SHORT).show()
                    }
                },
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Text
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 100.dp)
                    .height(50.dp)
            ) {
                Text(text = "Continue", color = Primary)
            }
            Spacer(modifier = Modifier.height(168.dp))
        }

        // The back button remains unchanged
        IconButton(
            onClick = onBackClicked,
            modifier = Modifier
                .padding(start = 16.dp, top = 48.dp) // Add padding for status bar
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Back to Login",
                tint = Text
            )
        }
    }
}
