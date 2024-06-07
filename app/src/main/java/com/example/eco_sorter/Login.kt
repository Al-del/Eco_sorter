package com.example.eco_sorter

import Eco_SorterTheme
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ip
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.HttpUrl
import okhttp3.Callback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
    import okhttp3.Call
    import okhttp3.Response
    import okio.IOException

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Eco_SorterTheme {

                LoginScreen()
            }
        }
    }
}

@Composable
fun LoginScreen() {
    val usernameState = remember { mutableStateOf("") } // Create a mutable state for the username
    val passwordState = remember { mutableStateOf("") } // Create a mutable state for the password
    val coroutineScope = rememberCoroutineScope() // Create a coroutine scope
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
                    val okHttpClient = OkHttpClient()

                    val httpUrl = HttpUrl.Builder()
                        .scheme("http")
                        .host(ip)
                        .port(5000)
                        .addPathSegment("login")
                        .addQueryParameter("username", usernameState.value)
                        .addQueryParameter("password", passwordState.value)
                        .build()

                    val request = Request.Builder()
                        .url(httpUrl)
                        .build()

                    okHttpClient.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            coroutineScope.launch(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    "server down",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        @Throws(IOException::class)
                        override fun onResponse(call: Call, response: Response) {
                            if (response.isSuccessful) {
                                val responseBody = response.body?.string()
                                val number = responseBody?.toIntOrNull() ?: responseBody?.toDoubleOrNull()
                                coroutineScope.launch(Dispatchers.Main) {

                                    if (number != null) {
                                        Toast.makeText(
                                            context,
                                            "Loged in succesful",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val intent = Intent(context, MainActivity::class.java)
                                        //add the user and points to the intent
                                        //Set precise with  2 decimal points
                                        val numero_nush_ = number

                                        intent.putExtra("user", usernameState.value)
                                        intent.putExtra("points", numero_nush_.toString())
                                        context.startActivity(intent)
                                    }else{
                                        Toast.makeText(
                                            context,
                                            "$responseBody",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }
                                }
                            }
                        }
                    })

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
                    val intent = Intent(context, Main::class.java)
                    context.startActivity(intent)
                }
            )
        }
    }
}