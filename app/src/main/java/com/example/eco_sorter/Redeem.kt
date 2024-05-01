package com.example.eco_sorter

import Butt
import Eco_SorterTheme
import Verf
import adrr_ip
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException
import kotlin.collections.Map

val butti = Butt()
class Redeem : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val username = intent.getStringExtra("user")
        var points = intent.getStringExtra("points")

        setContent {
                Eco_SorterTheme {
                    Log.d("Redeem_uderder", "Username: $username, Points: $points")
                    val buti= Butt()

                    if (username != null) {
                        buti.Myapp(id = 2, username = username, points =points )
                        if(username == "null"){
                            Verf(username, "Please login to redeem points")
                        }else{
                            //Verify if points is int on doubl

                            Redeem_codes(username, points)
                        }
                    }


                }

        }
    }
}
@Composable
fun Myapp__(navController: NavController, username: String, points: Int) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        //Create a BOttom_buttons.kt file and paste the AnimatedBottomNavigation function there
        // Your other Composables go here
        AnimatedBottomNavigation__(navController, username, points)


    }
}


@Composable
fun AnimatedBottomNavigation__(navController: NavController, username: String, points: Int) {
    // Make items a list of pairs with the label and their corresponding icon
    val context = LocalContext.current // Get the current context
    val items = listOf(
        Pair("Grants", Icons.Default.Home),
        Pair("Map", Icons.Default.Person),
        Pair("Redeem", Icons.Default.Settings)
    )

    val selectedIndex = remember { mutableStateOf(2) }

    BottomNavigation {
        items.forEachIndexed { index, item ->
            val isSelected = selectedIndex.value == index
            val animatedVisibility = animateFloatAsState(if (isSelected) 1f else 0f)

            BottomNavigationItem(
                icon = { Icon(item.second, contentDescription = null) },
                label = {
                    AnimatedVisibility(visible = isSelected) {
                        androidx.compose.material.Text(
                            text = item.first,
                            modifier = Modifier.graphicsLayer {
                                alpha = animatedVisibility.value
                            }
                        )
                    }
                },
                selected = isSelected,
                onClick = {
                    selectedIndex.value = index
                    when (index) {
                        0 -> {
                            val intent = Intent(context, Main::class.java)
                            intent.putExtra("user",username)
                            intent.putExtra("points",points.toString())
                            context.startActivity(intent)

                        }
                        1 -> {
                            val intent = Intent(context, Map::class.java)
                            intent.putExtra("user",username)
                            intent.putExtra("points",points.toString())
                            context.startActivity(intent)
                        }
                        2 -> {
                            val intent = Intent(context, Redeem::class.java)
                            intent.putExtra("user",username)
                            intent.putExtra("points",points.toString())
                            context.startActivity(intent)
                        }
                    }
                }
            )
        }
    }
}
@Composable
fun Redeem_codes(username:String,points:String?){
    var textValue by remember { mutableStateOf("") }
   val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val pointsValue = points?.toDoubleOrNull() ?: points?.toIntOrNull()

    if (pointsValue == null) {
        println("The points variable could not be converted to a number")
        return
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Here you can type the text shown in the screen nearby to redeem the points")
        TextField(
            value = textValue,
            onValueChange = { textValue = it },
            label = { Text("Enter redeem code") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f)
        )
        Button(onClick = {
            val editText: EditText? = null
            val button: Button? = null


            var okHttpClient: OkHttpClient? = null
            okHttpClient = OkHttpClient()


            // we add the information we want to send in
            // a form. each string we want to send should
            // have a name. in our case we sent the
            // dummyText with a name 'sample'
            val formbody: RequestBody = FormBody.Builder()
                .add("username", username)
                .add("code",textValue)
                .build()

            // while building request
            // we give our form
            // as a parameter to post()
            val request: Request = Request.Builder().url(adrr_ip+"redeem")
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
                    if (response.isSuccessful) {
                        coroutineScope.launch(Dispatchers.Main) {
                            Log.d("Redeem", "Response: ${response.body?.string()}")
                            var puncte = response.body?.string()
                            puncte = puncte.toString()
                            Toast.makeText(
                                context,
                                puncte,
                                Toast.LENGTH_SHORT
                            ).show()
                                //Add 100 to puncte
                                puncte = (puncte.toDouble() + 100).toString()
                                val intent = Intent(context, MainActivity::class.java)
                                intent.putExtra("user", username)
                                intent.putExtra("points", puncte)
                                context.startActivity(intent)

                        }
                    }
                }
            })


        }, modifier = Modifier.fillMaxWidth(0.8f).height(60.dp)) {
            Text("Redeem")
        }
    }
}