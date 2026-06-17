package com.example.fridgeassistant.ui.theme

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

@Composable
fun FoodEntryScreen(navController: NavController) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    var foodName by remember { mutableStateOf(TextFieldValue("")) }
    var quantity by remember { mutableStateOf(TextFieldValue("")) }
    var expiryDate by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            expiryDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
        }, year, month, day
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = foodName,
            onValueChange = { foodName = it },
            label = { Text("Food Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text("Quantity") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = { datePickerDialog.show() }) {
            Text(text = if (expiryDate.isEmpty()) "Select Expiry Date" else "Expiry: $expiryDate")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            val foodData = hashMapOf(
                "name" to foodName.text,
                "quantity" to quantity.text,
                "expiryDate" to expiryDate
            )

            if (currentUser != null) {
                db.collection("users").document(currentUser.uid)
                    .collection("food")
                    .add(foodData)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Food Item Added!", Toast.LENGTH_SHORT).show()
                        navController.navigate("food_list")
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Error Adding Item", Toast.LENGTH_SHORT).show()
                    }
            }
        }) {
            Text(text = "Save Food Item")
        }
    }
}
