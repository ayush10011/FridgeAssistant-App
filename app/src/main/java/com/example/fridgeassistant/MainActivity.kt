package com.example.fridgeassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.fridgeassistant.ui.theme.FridgeAssistantTheme
import com.example.fridgeassistant.ui.theme.navigation.NavGraph
import com.google.firebase.auth.FirebaseAuth
import android.util.Log

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FridgeAssistantTheme {
                val navController = rememberNavController()
                val auth = FirebaseAuth.getInstance()
                val startDestination = if (auth.currentUser != null) "home" else "login"

                Log.d("MainActivity", "Start Destination: $startDestination")

                NavGraph(navController = navController, startDestination = startDestination)
            }
        }
    }
}
