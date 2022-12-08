package com.example.cv2.database.repository

import com.example.cv2.database.dao.ContactDao
import com.example.cv2.data.entity.Contact

class ContactRepository(
    private val contactDao: ContactDao
) {

    fun getAll(): MutableList<Contact> {
        return contactDao.getAll()
    }

    fun getAllByUserId(userId: Int): MutableList<Contact> {
        return contactDao.getAllByUserId(userId)
    }

    suspend fun insert(
        contacts: List<Contact>
    ) {
        contactDao.insert(contacts);
    }

}