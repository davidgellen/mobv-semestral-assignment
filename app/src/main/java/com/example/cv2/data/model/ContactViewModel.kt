package com.example.cv2.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.cv2.PubApplication
import com.example.cv2.data.entity.Contact
import com.example.cv2.database.repository.ContactRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ContactViewModel(
    private val contactRepository: ContactRepository,
    private val application: PubApplication
): ViewModel() {

    private var _contacts = MutableLiveData<MutableList<Contact>>()

    val entries: LiveData<MutableList<Contact>>
        get() = _contacts

    fun getApplication(): PubApplication {
        return application
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @DelicateCoroutinesApi
    fun fetchAllFriends() {
        GlobalScope.launch(Dispatchers.Main) {
            val resp = contactRepository.getAll()
            _contacts.value = resp
        }
    }

}


class ContactViewModelFactory(
    private val contactRepository: ContactRepository,
    private val application: PubApplication
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContactViewModel(contactRepository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}