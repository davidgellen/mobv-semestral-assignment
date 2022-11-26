package com.example.cv2

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        setupActionBarWithNavController(navHostFragment.navController)
        navController = navHostFragment.navController
        val graph = navController.navInflater.inflate(R.navigation.app_nav)

        val sharedPreference = getSharedPreferences(
            "PREFERENCE_NAME", Context.MODE_PRIVATE)
        val accessToken = "Bearer " + sharedPreference?.getString("access", null)
        if (accessToken != null) {
            Log.i("ACTIVITY ON CREATE", "access token set, redirecting to menu")
            graph.setStartDestination(R.id.allEntriesFragment)
        } else {
            Log.i("ACTIVITY ON CREATE", "no access token, redirecting to login")
            // TODO: refresh token
            graph.setStartDestination(R.id.loginFragment)
        }
        navController.setGraph(graph, intent.extras)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}