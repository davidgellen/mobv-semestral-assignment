package com.example.cv2.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cv2.dao.ContactDao
import com.example.cv2.data.entity.Contact

class ContactViewModel(
    private val contactDao: ContactDao
): ViewModel() {

    private var _contacts = MutableLiveData<MutableList<Contact>>()

    val entries: LiveData<MutableList<Contact>>
        get() = _contacts

    fun setContacts(
        list: MutableList<Contact>
    ) {
        _contacts.value = list
    }
}

class ContactViewModelFactory(
    private val contactDao: ContactDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContactViewModel(contactDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}