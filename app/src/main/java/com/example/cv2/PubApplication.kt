package com.example.cv2

import android.app.Application
import com.example.cv2.database.AppDatabase
import com.example.cv2.database.repository.ContactRepository
import com.example.cv2.database.repository.PubRepository

class PubApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    val pubRepository by lazy { PubRepository(database.pubDao()) }
    val contactRepository by lazy { ContactRepository(database.contactDao()) }
}