package com.example.eco_sorter

import User
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.example.eco_sorter.ui.theme.Eco_SorterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Eco_SorterTheme {
                SignUpScreen()
            }
        }
    }
}

@Composable
fun SignUpScreen() {
    val usernameState = remember { mutableStateOf("") } // Create a mutable state for the username
    val passwordState = remember { mutableStateOf("") } // Create a mutable state for the password
    val context = LocalContext.current // Get the current context

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC2F0FF)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Create Account",
                style = TextStyle(color = Color(0xFF29ABCA), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = usernameState.value, // Set the value to the current value of the username state
                onValueChange = { usernameState.value = it }, // Update the username state when the user types into the field
                label = { Text("Username", color = Color(0xFF29ABCA), fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(0.8f),
                textStyle = TextStyle(color = Color(0xFF333333)),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = passwordState.value, // Set the value to the current value of the password state
                onValueChange = { passwordState.value = it }, // Update the password state when the user types into the field
                label = { Text("Password", color = Color(0xFF29ABCA), fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(0.8f),
                textStyle = TextStyle(color = Color(0xFF333333)),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val user = User()
                    user.username = usernameState.value
                    user.password = passwordState.value
                    user.register()
                    //Show a toast message
                    Toast.makeText(context, "Account created successfully. Now ya can login", Toast.LENGTH_SHORT).show()

                },
                modifier = Modifier.fillMaxWidth(0.8f).height(60.dp), // Adjusted height
                colors = ButtonDefaults.buttonColors(contentColor = Color.Red) // Adjusted backgroundColor
            ) {
                Text("Sign Up", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Already have an account? Login",
                style = TextStyle(color = Color(0xFF333333), fontSize = 16.sp),
                modifier = Modifier.align(Alignment.CenterHorizontally).clickable {
                    val intent = Intent(context, Login::class.java)
                    context.startActivity(intent)
                }
            )
        }
    }
}