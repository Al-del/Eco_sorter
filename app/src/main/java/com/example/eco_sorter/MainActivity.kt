package com.example.eco_sorter

import Butt
import Eco_SorterTheme
import Verf
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Slider
import androidx.compose.material.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import kotlin.collections.Map


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var username: String = "Unknown User"
        var points: String = "0"
        try {
             username = intent.getStringExtra("user").toString()
             points = intent.getStringExtra("points").toString()
            if(username == null) {
                username = "Unknown User"
                points = "0"
            }
        }catch (e: Exception) {
         Log.d("error","Not loged in")
        }


        val buti = Butt()
        setContent {
            Eco_SorterTheme {
                    buti.Myapp(id = 0, username = username, points =points )
                    Labelus(points,username)
                    Log.d("points",points)


            }
        }
    }
}


@Composable
fun MyApp(navController: NavController, username: String, points: Int) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        //Create a BOttom_buttons.kt file and paste the AnimatedBottomNavigation function there
        // Your other Composables go here
        AnimatedBottomNavigation(navController, username, points)
    }
}

@Composable
fun AnimatedBottomNavigation(navController: NavController, username: String, points: Int) {
    // Make items a list of pairs with the label and their corresponding icon
    val context = LocalContext.current // Get the current context
    val items = listOf(
        Pair("Grants", Icons.Default.Home),
        Pair("Map", Icons.Default.Person),
        Pair("Redeem", Icons.Default.Settings)
    )

    val selectedIndex = remember { mutableStateOf(0) }

    BottomNavigation {
        items.forEachIndexed { index, item ->
            val isSelected = selectedIndex.value == index
            val animatedVisibility = animateFloatAsState(if (isSelected) 1f else 0f)

            BottomNavigationItem(
                icon = { Icon(item.second, contentDescription = null) },
                label = {
                    AnimatedVisibility(visible = isSelected) {
                        Text(
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
fun Labelus(Points:String?,username:String?) {
    val context = LocalContext.current
    if (Points != "null" && Points != null) {

            Text(text = Points,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.TopEnd)
                    .offset(y = (15).dp, x = (-15).dp)
        ,
                color = Color(0xFF29ABCA),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold)
        Text(text = "$username",
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.TopStart)
                .offset(y = (15).dp, x = (15).dp)
            ,
            color = Color(0xFF29ABCA),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold)

        GrantButtons(Points,username)
    }else{
        if (username != null) {
            Verf(username,"You can't see your code if you are not loged in!")
        }
    }
}
@Composable
fun GrantButtons(Points: String?, user:String?) {
    val context = LocalContext.current // Get the current context
    val screenHeight = LocalConfiguration.current.screenHeightDp
    var showSlider by remember { mutableStateOf(false) }
    var showSlider_ by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Row(
            modifier = Modifier.offset(y = (screenHeight / 4).dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    // Handle the "Grant Electricity" button click event
                    showSlider_ = true
                    Toast.makeText(context, "Grant Electricity button clicked", Toast.LENGTH_SHORT)
                        .show()
                },
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Text("Grant Electricity")
            }

            Button(
                onClick = {
                    // Handle the "Grants NG" button click event
                    showSlider_ = true
                    Toast.makeText(context, "Grants NG button clicked", Toast.LENGTH_SHORT).show()

                }
            ) {
                Text("Grants NG")
            }
        }
        if (showSlider) {
            if (user != null) {
                SliderExample(Max_size = Points, user = user, txt = "Grants electricity")
            }
        } else if (showSlider_) {
            if (user != null) {
                SliderExample(Max_size = Points, user = user, txt = "Grants NG")
            }
        }
    }
}
@Composable
fun SliderExample(Max_size: String?, user:String, txt: String? = null) {
    val couroutineScope = rememberCoroutineScope() // Create a coroutine scope
    val context = LocalContext.current // Get the current context

    if (Max_size != null) {
        if(Max_size.toFloat()>0) {
            var sliderPosition by remember { mutableStateOf(0f) }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    if (Max_size != null) {
                        Slider(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = sliderPosition,
                            onValueChange = { newValue ->
                                sliderPosition = newValue
                            },
                            valueRange = 0f..Max_size.toFloat(),
                            steps = 0,
                            onValueChangeFinished = {
                                // You can use this to trigger some action when the user finishes interacting with the slider
                            }
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = sliderPosition.toString(),
                            color = Color(0xFF29ABCA),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(onClick ={
                        val numero_nush=(Max_size.toDouble()-sliderPosition.toDouble())
                        //Set precision to 2 decimal places
                        val numero_nush_ = String.format("%.2f", numero_nush)
                        if(user == "Grants electricity") {
                            push_(
                                "red",
                                user = user,
                                Max_size,
                                0,
                                sliderPosition,
                                context,
                                couroutineScope
                            )
                            //GO to MainActivity
                            val intent = Intent(context, MainActivity::class.java)
                            intent.putExtra("user", user)
                            intent.putExtra("points",numero_nush_.toString())
                            context.startActivity(intent)
                        }
                        else{
                            push__("red", user =user , Max_size, sliderPosition, 0,context,couroutineScope)
                            val intent = Intent(context, MainActivity::class.java)
                            intent.putExtra("user", user)
                            intent.putExtra("points", numero_nush_.toString())
                            context.startActivity(intent)

                        }
                    }) {
                        Text(txt ?: "Send")
                    }
                }
            }
        }
    }
}
fun push_(path:String, user:String, points:String, red_gaz: Int, red_electricitate: Float, context: Context, coroutineScope: CoroutineScope){

    val okHttpClient = OkHttpClient()

    val httpUrl = HttpUrl.Builder()
        .scheme("http")
        .host(ip)
        .port(5000)
        .addPathSegment(path)
        .addQueryParameter("user", user)
        .addQueryParameter("points", points)
        .addQueryParameter("red_gaz", red_gaz.toString())
        .addQueryParameter("red_electricitate", red_electricitate.toString())
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
                            "Data sent successfully",
                            Toast.LENGTH_SHORT
                        ).show()

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

}



fun push__(path:String, user:String, points:String, red_gaz: Float, red_electricitate: Int, context: Context, coroutineScope: CoroutineScope){

    val okHttpClient = OkHttpClient()

    val httpUrl = HttpUrl.Builder()
        .scheme("http")
        .host(ip)
        .port(5000)
        .addPathSegment(path)
        .addQueryParameter("user", user)
        .addQueryParameter("points", points)
        .addQueryParameter("red_gaz", red_gaz.toString())
        .addQueryParameter("red_electricitate", red_electricitate.toString())
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
                            "Data sent successfully",
                            Toast.LENGTH_SHORT
                        ).show()

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

}