package com.example.cv2.database.repository

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.cv2.database.dao.ContactDao
import com.example.cv2.data.entity.Contact
import com.example.cv2.data.response.ContactResponseBody
import com.example.cv2.mapper.ContactResponseToEntityMapper
import com.example.cv2.service.RetrofitFriendApi
import com.example.cv2.utils.ConnectivityUtils

class ContactRepository(
    private val contactDao: ContactDao,
    private val context: Context
) {

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getAll(): MutableList<Contact> {
        if (ConnectivityUtils().isOnline(context)) {
            Log.i("GET ALL FRIENDS", "FROM SERVICE")
            return fetchFromService()
        } else {
            Log.i("GET ALL FRIENDS", "FROM DATABASE")
            return contactDao.getAll()
        }
    }

    fun getAllByUserId(userId: Int): MutableList<Contact> {
        return contactDao.getAllByUserId(userId)
    }

    suspend fun insert(
        contacts: List<Contact>
    ) {
        contactDao.insert(contacts);
    }

    private suspend fun fetchFromService() : MutableList<Contact> {
        val sharedPreference = context.getSharedPreferences(
            "PREFERENCE_NAME", Context.MODE_PRIVATE)
        val accessToken = "Bearer " + (sharedPreference?.getString("access", "defaultAccess") ?: "defaultAccess")
        val uid = sharedPreference?.getString("uid", "defaultUid") ?: "defaultUid"
        val response: List<ContactResponseBody> = RetrofitFriendApi.RETOROFIT_SERVICE
            .allFriends(accessToken , uid)
        val contacts: MutableList<Contact> = ContactResponseToEntityMapper().entryListToPubList(
            response.toMutableList(), uid.toLong())
        insert(contacts)
        return contacts
    }

}