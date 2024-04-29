import android.annotation.SuppressLint
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import android.content.Intent
import android.provider.DocumentsContract.Document
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.eco_sorter.Login
import com.example.eco_sorter.Main
import com.example.eco_sorter.MainActivity
import com.example.eco_sorter.Map
import com.example.eco_sorter.Redeem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class Butt {
    @Composable
    fun Myapp(id:Int, username: String, points: String?) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            AnimatedBottomNavigation(id,username, points)

        }
    }
    @Composable
    fun AnimatedBottomNavigation(id:Int, username: String, points: String?) {
        // Make items a list of pairs with the label and their corresponding icon
        val context = LocalContext.current // Get the current context
        val items = listOf(
            Pair("Grants", Icons.Default.Home),
            Pair("Map", Icons.Default.Person),
            Pair("Redeem", Icons.Default.Settings)
        )

        val selectedIndex = remember { mutableStateOf(id) }

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
                                val intent = Intent(context, MainActivity::class.java)
                                intent.putExtra("user",username)
                                intent.putExtra("points",points.toString())
                                context.startActivity(intent)

                            }
                            1 -> {
                                val intent = Intent(context, com.example.eco_sorter.Map::class.java)
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
}
class User {
    var username: String = ""
    var password: String = ""
    var points = 0


}

@Composable
fun Verf(user: String,msj:String): String {
    val context = LocalContext.current
    var result = "Yup"
    if (user == "null") {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = msj,
                modifier = Modifier.align(Alignment.Center).offset(y = (-100).dp),
                color = Color.Red
            )
            Button(
                onClick = {
                    val intent = Intent(context, Main::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(text = "Go  to login")
            }
        }
        result = "No"
    }
    return result
}
