package com.example.cv2.repository

import androidx.lifecycle.LiveData
import com.example.cv2.dao.ContactDao
import com.example.cv2.data.entity.Contact

class ContactRepository(
    private val contactDao: ContactDao
) {

    fun getAll(): LiveData<List<Contact>> {
        return contactDao.getAll()
    }

}