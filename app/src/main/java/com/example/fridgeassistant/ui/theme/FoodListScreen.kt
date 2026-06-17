package com.example.fridgeassistant.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import androidx.navigation.NavController


@Composable
fun FoodListScreen(navController: NavController) {
    val foodItems = remember { mutableStateListOf<FoodItem>() }
    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid  // Get logged-in user ID

    LaunchedEffect(Unit) {
        if (userId != null) {
            db.collection("users").document(userId).collection("food")
                .get()
                .addOnSuccessListener { result ->
                    foodItems.clear()  // Clear old data
                    for (document in result) {
                        val name = document.getString("name") ?: "Unknown"
                        val quantity = document.getString("quantity") ?: "Unknown"
                        val expiryDate = document.getString("expiryDate") ?: "Unknown"
                        foodItems.add(FoodItem(document.id, name, quantity, expiryDate))
                    }
                    Log.d("FoodListScreen", "Fetched ${foodItems.size} food items")
                }
                .addOnFailureListener { exception ->
                    Log.e("FoodListScreen", "Error fetching food items: ${exception.message}")
                }
        } else {
            Log.e("FoodListScreen", "User not logged in!")
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Your Food Items", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        LazyColumn {
            items(foodItems) { foodItem ->
                FoodItemRow(foodItem, onDelete = {
                    db.collection("users").document(userId!!)
                        .collection("food").document(foodItem.id)
                        .delete()
                        .addOnSuccessListener {
                            foodItems.remove(foodItem)
                            Log.d("FoodListScreen", "Deleted food item: ${foodItem.name}")
                        }
                        .addOnFailureListener { exception ->
                            Log.e("FoodListScreen", "Error deleting food item: ${exception.message}")
                        }
                })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("home") }) {
            Text("Back to Home")
        }
    }
}

@Composable
fun FoodItemRow(foodItem: FoodItem, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Name: ${foodItem.name}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("Quantity: ${foodItem.quantity}", fontSize = 14.sp)
            Text("Expiry: ${foodItem.expiryDate}", fontSize = 14.sp)
        }
        Button(onClick = onDelete, colors = ButtonDefaults.buttonColors(Color.Red)) {
            Text("Delete", color = Color.White)
        }
    }
}

data class FoodItem(val id: String, val name: String, val quantity: String, val expiryDate: String)