package com.example.fridgeassistant.repository

import android.util.Log
import com.example.fridgeassistant.ui.theme.FoodItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FoodRepository {
    private val db = FirebaseFirestore.getInstance()
    private val foodCollection = db.collection("foodItems")

    // Add a new food item
    suspend fun addFoodItem(food: FoodItem) {
        try {
            foodCollection.add(food).await()
            Log.d("Firestore", "Food item added successfully")
        } catch (e: Exception) {
            Log.e("Firestore", "Error adding food", e)
        }
    }

    // Delete a food item by ID
    suspend fun deleteFoodItem(id: String) {
        try {
            foodCollection.document(id).delete().await()
            Log.d("Firestore", "Food item deleted")
        } catch (e: Exception) {
            Log.e("Firestore", "Error deleting food", e)
        }
    }
}
