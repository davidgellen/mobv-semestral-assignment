package com.example.cv2

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.cv2.data.request.RefreshTokenRequestBody
import com.example.cv2.data.response.RegisterResponseBody
import com.example.cv2.service.RetrofitUserApi
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        setupActionBarWithNavController(navHostFragment.navController)
        navController = navHostFragment.navController
        val graph = navController.navInflater.inflate(R.navigation.app_nav)

        if (this.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED &&
            this.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) } != PackageManager.PERMISSION_GRANTED) {
            this.let { ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101) }
        }

        val sharedPreference = getSharedPreferences(
            "PREFERENCE_NAME", Context.MODE_PRIVATE
        )

//        val editor = sharedPreference?.edit()
//        editor?.clear()
//        editor?.apply()

        val accessToken = sharedPreference?.getString("access", null)

        if (accessToken != null) {
            Log.e("ACTIVITY ON CREATE", "HAS ACCESS TOKEN, REDIRECT TO ALL ENTRIES")
            graph.setStartDestination(R.id.allEntriesFragment)
        } else {
            Log.e("ACTIVITY ON CREATE", "no access token, redirecting to login")
            graph.setStartDestination(R.id.loginFragment)
        }
        navController.setGraph(graph, intent.extras)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}