package com.example.eco_sorter

import User
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.eco_sorter.ui.theme.Eco_SorterTheme
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Eco_SorterTheme {
                val database = Firebase.database
                val myRef = database.getReference()
                LoginScreen(myRef)
            }
        }
    }
}

@Composable
fun LoginScreen(databaseReference: DatabaseReference) {
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
                text = "Login",
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

                },
                modifier = Modifier.fillMaxWidth(0.8f).height(60.dp), // Adjusted height
                colors = ButtonDefaults.buttonColors(contentColor = Color.Red) // Adjusted backgroundColor
            ) {
                Text("Login", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Don't have an account? Sign Up",
                style = TextStyle(color = Color(0xFF333333), fontSize = 16.sp),
                modifier = Modifier.align(Alignment.CenterHorizontally).clickable {
                    val intent = Intent(context, Login::class.java)
                    context.startActivity(intent)
                }
            )
        }
    }
}