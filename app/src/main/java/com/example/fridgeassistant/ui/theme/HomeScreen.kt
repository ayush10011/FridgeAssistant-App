package com.example.fridgeassistant.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Fridge Assistant") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Welcome to FridgeAssistant", style = MaterialTheme.typography.h5)

            Spacer(modifier = Modifier.height(20.dp))

            // Button to navigate to FoodEntryScreen
            Button(onClick = { navController.navigate("food_entry") }) {
                Text("Add Food Item")
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Button to navigate to FoodListScreen
            Button(onClick = { navController.navigate("food_list") }) {
                Text("View Food Inventory")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Logout Button
            Button(
                onClick = {
                    auth.signOut()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true } // Clears back stack
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
            ) {
                Text("Logout")
            }
        }
    }
}
