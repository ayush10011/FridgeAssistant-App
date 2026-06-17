package com.example.fridgeassistant.model

data class FoodItem(
    val id: String = "",  // Required for Firestore document ID
    val name: String = "",
    val quantity: Int = 0,
    val expiryDate: String = ""
)
