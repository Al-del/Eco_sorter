package com.example.eco_sorter

import Butt
import Eco_SorterTheme
import Verf
import adrr_ip
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okio.IOException
import kotlin.collections.Map
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.lang.reflect.Type
import android.Manifest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import org.osmdroid.config.Configuration
import org.osmdroid.config.IConfigurationProvider
import java.io.File

class Map : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val username = intent.getStringExtra("user")
        val points = intent.getStringExtra("points")
        val buti = Butt()
        val Latti = ArrayList<Float>()
        val Longi = ArrayList<Float>()
        setContent {
            Eco_SorterTheme {
                if (username != null) {
                    buti.Myapp(id = 1, username = username, points =points )
                    //Get the Latti and Longi with the help of on_load function
                    on_load {
                        for ((key, value) in it) {
                            Log.d("Map", "Key: $key, Value: $value")
                            Latti.add(value.toString().toFloat())
                            Longi.add(value.toString().toFloat())
                        }
                    }
                    MapViewContainer(Latti, Longi)
                }
            }
        }
    }
}
@Composable
fun on_load(onMapReceived: (Map<String, Any>) -> Unit) {
    val context = LocalContext.current // Get the current context
    val coroutineScope = rememberCoroutineScope()
    var okHttpClient: OkHttpClient? = null
    okHttpClient = OkHttpClient()

    val formbody: RequestBody = FormBody.Builder().build()

    val request: Request = Request.Builder().url(adrr_ip+"location")
        .post(formbody)
        .build()

    okHttpClient.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: java.io.IOException) {
            coroutineScope.launch(Dispatchers.Main) {
                Toast.makeText(context, "server down", Toast.LENGTH_SHORT).show()
            }
        }

        @Throws(java.io.IOException::class)
        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                coroutineScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, "data received", Toast.LENGTH_SHORT).show()
                }

                val gson = Gson()
                val type: Type = object : TypeToken<Map<String, Any>>() {}.type
                val responseString = response.body?.string() // This is your JSON string
                val myMap: Map<String, Any> = gson.fromJson(responseString, type)

                // Call the callback function with the map
                onMapReceived(myMap)
            }
        }
    })
}
fun checkLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
}
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapViewContainer(Latti: ArrayList<Float>, Longi: ArrayList<Float>) {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }
    AndroidView(factory = { context ->
        // Set the user agent to prevent getting blocked by the tile servers
        Configuration.getInstance().userAgentValue = context.packageName

        // Set the osmdroid base path to a location where the app has write access
        val osmDroidDirectory = File(context.filesDir, "osmdroid")
        Configuration.getInstance().osmdroidBasePath = osmDroidDirectory

        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setBuiltInZoomControls(true)
            setMultiTouchControls(true)

            // Check if the location permission has been granted
            if (permissionState.hasPermission) {
                try {
                    // Get the current location
                    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val locationProvider = LocationManager.GPS_PROVIDER
                    val lastKnownLocation = locationManager.getLastKnownLocation(locationProvider)
                    val currentLocation = GeoPoint(lastKnownLocation?.latitude ?: 0.0, lastKnownLocation?.longitude ?: 0.0)

                    // Create a marker and set its position to the current location
                    val marker = Marker(this)
                    marker.position = currentLocation
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                    //Get all the locatio nform Latte and Longi
                    for (i in Latti.indices) {
                        val latitude = Latti[i]
                        val longitude = Longi[i]
                        Log.d("Map", "Latitude: ${latitude.toDouble()}, Longitude: ${longitude.toDouble()}")
                        //Make an array of GeoPoints
                        val geoPoints = GeoPoint(latitude.toDouble(), longitude.toDouble())
                        val marker = Marker(this)
                        marker.position = geoPoints
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        marker.title = "Title"
                        marker.snippet = "Description"
                        marker.infoWindow = null
                        marker.setOnMarkerClickListener { marker, mapView ->
                            // Handle the marker click event
                            true
                        }
                    }
                    val geoPoints = GeoPoint(44.183028, 28.649457)
                    val marker_ = Marker(this)
                    marker_.position = geoPoints
                    marker_.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    marker_.title = "Title"
                    marker_.snippet = "Description"
                    marker_.infoWindow = null

                    overlays.add(marker_)


                    overlays.add(marker)

                    // Center the map on the current location and set the zoom level
                    getController().animateTo(currentLocation)

                    getController().setZoom(18.0) // Adjust the zoom level as needed
                } catch (e: SecurityException) {
                    // Handle the SecurityException
                }
            }
        }
    })
}