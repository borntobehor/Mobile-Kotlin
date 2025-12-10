package com.example.learnkotlin

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
// 1. Import components for the back button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// 2. Import LocalContext for showing Toasts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnkotlin.ui.theme.Primary
import com.example.learnkotlin.R
import com.example.learnkotlin.ui.theme.Text
import com.example.learnkotlin.ui.theme.White

// 3. Update the function to accept navigation callbacks
@Composable
fun OtpScreen(
    onBackClicked: () -> Unit,
    onContinueClicked: () -> Unit
) {

    var otpValue by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current

    // 4. Use a Box to layer the back button over the content
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Primary),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(155.dp))

            Image(
                painter = painterResource(R.drawable.all_1), contentDescription = "Logo",
                modifier = Modifier.size(150.dp)
            )

            Text(
                text = "Enter code send to your\nemail",
                color = Text,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(42.dp))

            BasicTextField(
                value = otpValue,
                onValueChange = { newValue ->
                    if (newValue.text.length <= 6 && newValue.text.all { it.isDigit() }) {
                        otpValue = newValue
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword
                ),
                decorationBox = {
                    Row(horizontalArrangement = Arrangement.Center) {
                        repeat(6) { index ->
                            val char = when {
                                index < otpValue.text.length -> otpValue.text[index]
                                else -> ""
                            }
                            Column(
                                modifier = Modifier
                                    .width(45.dp)
                                    .height(50.dp)
                                    .padding(horizontal = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = char.toString(),
                                    color = Text,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(Text)
                                )
                            }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = { /* TODO: Handle Resend Code logic */ }) {
                Text(
                    text = "Resend Code",
                    color = Text,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 5. Update the "Continue" button's onClick logic
            Button(
                onClick = {
                    if (otpValue.text.length == 6) {
                        // This executes the navigation command from MainActivity
                        onContinueClicked()
                    } else {
                        Toast.makeText(context, "Please enter a valid 6-digit OTP.", Toast.LENGTH_SHORT).show()
                    }
                },
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 80.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "Continue",
                    color = Primary
                )
            }
            Spacer(modifier = Modifier.height(140.dp))
        }

        // 6. Add the IconButton for the back action
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
