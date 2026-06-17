package com.example.fridgeassistant  // Ensure this matches your package name

import android.app.Application
import com.google.firebase.FirebaseApp

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)  // Initialize Firebase
    }
}
