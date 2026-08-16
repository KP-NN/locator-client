package ru.kpnn.locator_client

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.kpnn.locator_client.ui.theme.LocatorclientTheme

import android.content.pm.PackageManager
import android.location.Location
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.CancellationTokenSource

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var coordinatesTextView: TextView
    private lateinit var btnGetLocation: Button
    private val locationPermissionRequestCode = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        coordinatesTextView = findViewById(R.id.coordinatesTextView)
        btnGetLocation = findViewById(R.id.btnGetLocation)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        btnGetLocation.setOnClickListener {
            checkLocationPermissions()
        }
    }

    private fun checkLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionRequestCode
            )
        } else {
            fetchCurrentLocation()
        }
    }

    private fun fetchCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) return

        coordinatesTextView.text = "Updating location..."

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    coordinatesTextView.text = "Lat: ${location.latitude}, Lon: ${location.longitude}"
                } else {
                    coordinatesTextView.text = "Unable to find location."
                }
            }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LocatorclientTheme {
        Greeting("Android")
    }
}