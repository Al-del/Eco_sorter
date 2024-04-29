package com.example.eco_sorter
import Eco_SorterTheme
import User
import adrr_ip
import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.Callback
import org.jetbrains.annotations.NotNull
import java.io.IOException
class Main : ComponentActivity() {
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
    val coroutineScope = rememberCoroutineScope()
    var showToast by remember { mutableStateOf(false) }

    LaunchedEffect(showToast) {
        if (showToast) {
            Toast.makeText(context, "Account created successfully. Now ya can login", Toast.LENGTH_SHORT).show()
            showToast = false
        }
    }
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

                    val editText: EditText? = null
                    val button: Button? = null
                    val utilizator = User()
                    utilizator.username = usernameState.value
                    utilizator.password = passwordState.value

                    var okHttpClient: OkHttpClient? = null
                    okHttpClient = OkHttpClient()


                    // we add the information we want to send in
                    // a form. each string we want to send should
                    // have a name. in our case we sent the
                    // dummyText with a name 'sample'
                    val formbody: RequestBody = FormBody.Builder()
                        .add("username", utilizator.username)
                        .add("password", utilizator.password)
                        .add("points", utilizator.points.toString())
                        .add("discount_gaz", utilizator.discount_gaz.toString())
                        .add("discount_electricitate", utilizator.discount_electricitate.toString())
                        .build()

                    // while building request
                    // we give our form
                    // as a parameter to post()
                    val request: Request = Request.Builder().url(adrr_ip)
                        .post(formbody)
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
                            if (response.body?.string().equals("received")) {
                                coroutineScope.launch(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "data received",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    })




                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(60.dp), // Adjusted height
                colors = ButtonDefaults.buttonColors(contentColor = Color.Red) // Adjusted backgroundColor
            ) {
                Text("Sign Up", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Already have an account? Login",
                style = TextStyle(color = Color(0xFF333333), fontSize = 16.sp),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        val intent = Intent(context, Login::class.java)
                        context.startActivity(intent)
                    }
            )
        }
    }
}


