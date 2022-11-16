package com.example.cv2.application

import android.app.Application
import com.example.cv2.database.AppDatabase

class PubApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}