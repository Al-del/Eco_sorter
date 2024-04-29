package com.example.eco_sorter

import Butt
import Eco_SorterTheme
import Verf
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
        SliderExample(Points.toInt())

    }else{
        if (username != null) {
            Verf(username,"You can't see your code if you are not loged in!")
        }
    }
}
@Composable
fun SliderExample(Max_size:Int) {
    if(Max_size.toFloat()>0) {
        var sliderPosition by remember { mutableStateOf(0f) }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
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
            Text(
                text = sliderPosition.toString(), color = Color(0xFF29ABCA),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
                    .offset(y = (-25).dp, x = (0).dp)
            ) // This will display the current value of the slider
        }
    }

}