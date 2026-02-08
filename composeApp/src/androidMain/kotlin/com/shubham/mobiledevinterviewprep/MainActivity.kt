package com.shubham.mobiledevinterviewprep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.shubham.mobiledevinterviewprep.data.local.SettingsFactory
import com.google.firebase.FirebaseApp
import com.shubham.mobiledevinterviewprep.platform.AndroidAuthActivityHolder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        // Initialize platform-specific settings for bookmark persistence
        SettingsFactory.initialize(applicationContext)
        FirebaseApp.initializeApp(this)
        AndroidAuthActivityHolder.register(this)

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}