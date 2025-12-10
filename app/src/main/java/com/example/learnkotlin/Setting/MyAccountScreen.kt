package com.example.learnkotlin.Setting

import com.example.learnkotlin.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.learnkotlin.Components.BackAppBar
import com.example.learnkotlin.ui.theme.Primary
import com.example.learnkotlin.ui.theme.Text
import com.example.learnkotlin.ui.theme.White

// --- 1. Top App Bar ---
/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAccountAppBar(onBackClicked: () -> Unit) {
    TopAppBar(
        title = { Text("My Account", fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Primary,
            titleContentColor = Text,
            navigationIconContentColor = Text
        )
    )
}
*/

// --- 2. Profile Picture Section ---
@Composable
fun ProfilePictureSection(onPictureChangeClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onPictureChangeClicked() }
        ) {
            Box {
                Image(
                    painter = painterResource(id = R.drawable.new_1), // Replace with user's profile picture
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .align(Alignment.BottomEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Change Picture",
                        tint = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.Center)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tap to change profile picture",
                color = Text,
                fontSize = 12.sp
            )
        }
    }
}

// --- 3. Custom Text Field ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAccountTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Text) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedTextColor = Text,
            unfocusedTextColor = Text,
            focusedIndicatorColor = White,
            unfocusedIndicatorColor = White,
            cursorColor = Text
        )
    )
}


// --- START OF EDIT ---
// 4. Visual Transformation for Date of Birth
class DateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // We want to format the text as DD/MM/YYYY
        val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i % 2 == 1 && i < 4) out += "/"
        }

        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 3) return offset + 1
                if (offset <= 8) return offset + 2
                return 10
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                if (offset <= 10) return offset - 2
                return 8
            }
        }

        return TransformedText(AnnotatedString(out), offsetTranslator)
    }
}


// --- 5. Main Screen Composable ---
@Composable
fun MyAccountScreen(
    navController: NavController,
    onBackClicked: () -> Unit = {},
    onSaveClicked: () -> Unit = {},
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Primary,
        topBar = { BackAppBar(navController, "My Account") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            ProfilePictureSection(onPictureChangeClicked = { /* TODO: Handle image picking */ })

            Text(
                "Personal Information",
                color = Text,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                MyAccountTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = "First Name",
                    modifier = Modifier.weight(1f)
                )
                MyAccountTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = "Last Name",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            MyAccountTextField(value = email, onValueChange = { email = it }, label = "Email Address")
            Spacer(modifier = Modifier.height(8.dp))
            MyAccountTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Phone Numbers",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // --- START OF EDIT ---
            // Updated Date of Birth TextField
            MyAccountTextField(
                value = dob,
                onValueChange = {
                    // Only allow up to 8 digits
                    if (it.length <= 8) {
                        dob = it.filter { char -> char.isDigit() }
                    }
                },
                label = "Date Of Birth",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = DateVisualTransformation()
            )
            // --- END OF EDIT ---

            Spacer(modifier = Modifier.weight(1f)) // Pushes the button to the bottom

            Button(
                onClick = onSaveClicked,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = White,
                    contentColor = Primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Save", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

