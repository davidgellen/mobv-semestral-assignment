package com.example.cv2.mapper

import com.example.cv2.data.entity.Contact
import com.example.cv2.data.response.ContactResponseBody

class ContactResponseToEntityMapper {

    fun responseToEntity(
        contact: ContactResponseBody,
        userId: Long
    ) : Contact {
        return Contact(
            userId = userId,
            contactId = contact.userId.toLong(),
            contactName = contact.username,
            barId = contact.barId,
            barName = contact.barName,
            time = contact.time,
            barLat = contact.barLat,
            barLon = contact.barLon
        )
    }

    fun entryListToPubList(
        contactList: MutableList<ContactResponseBody>,
        userId: Long
    ) : MutableList<Contact> {
        val pubs = mutableListOf<Contact>()
        for (contact in contactList) {
            pubs.add(responseToEntity(contact, userId))
        }
        return pubs
    }
}