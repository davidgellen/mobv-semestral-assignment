package com.example.cv2.application

import android.app.Application
import com.example.cv2.database.AppDatabase
import com.example.cv2.repository.PubRepository

class PubApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    val pubRepository by lazy { PubRepository(database.pubDao()) }
}