package com.example.cv2.data.model

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.example.cv2.adapter.ContactAdapter
import com.example.cv2.application.PubApplication
import com.example.cv2.dao.ContactDao
import com.example.cv2.data.entity.Contact
import com.example.cv2.data.response.ContactResponseBody
import com.example.cv2.mapper.ContactResponseToEntityMapper
import com.example.cv2.repository.ContactRepository
import com.example.cv2.service.RetrofitFriendApi
import com.example.cv2.utils.ConnectivityUtils
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ContactViewModel(
    private val contactRepository: ContactRepository,
    private val application: PubApplication
): ViewModel() {

    private val context by lazy { application.applicationContext }

    private var _contacts = MutableLiveData<MutableList<Contact>>()

    private val connectivityUtils = ConnectivityUtils()

    val entries: LiveData<MutableList<Contact>>
        get() = _contacts

    fun getApplication() : PubApplication {
        return application
    }

    fun setContacts(
        list: MutableList<Contact>
    ) {
        _contacts.value = list
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @DelicateCoroutinesApi
    fun fetchAllFriends() {

        val sharedPreference = context.getSharedPreferences(
            "PREFERENCE_NAME", Context.MODE_PRIVATE)
        val accessToken = "Bearer " + (sharedPreference?.getString("access", "defaultAccess") ?: "defaultAccess")
        val uid = sharedPreference?.getString("uid", "defaultUid") ?: "defaultUid"
        if (connectivityUtils.isOnline(context!!)) {
            viewModelScope.launch(Dispatchers.Main) {
                val response: List<ContactResponseBody> = RetrofitFriendApi.RETOROFIT_SERVICE
                    .allFriends(accessToken , uid)
                val contacts: MutableList<Contact> = ContactResponseToEntityMapper().entryListToPubList(
                    response.toMutableList(), uid.toLong())
                contactRepository.insert(contacts)
                _contacts.value = contacts

            }
        } else {
            Log.e("NO INTERNET", "LOADING CONTACTS FROM DATABASE")
            val contacts: MutableList<Contact> = contactRepository.getAllByUserId(uid.toInt())
            _contacts.value = contacts
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