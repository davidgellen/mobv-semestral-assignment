package com.example.cv2.mapper

import com.example.cv2.data.entity.Pub
import com.example.cv2.data.jsonmapper.Entry

class EntryToPubMapper {

    fun entryToPub(
        entry: Entry
    ) : Pub {
        return Pub(
            id = 0,
            importedId = entry.id,
            lat = entry.lat,
            lon = entry.lon,
            name = entry.tags.name,
            openingHours = entry.tags.opening_hours,
            website = entry.tags.website,
            phone = entry.tags.phone
        )
    }

    fun entryListToPubList(
        entryList: MutableList<Entry>
    ) : MutableList<Pub> {
        val pubs = mutableListOf<Pub>()
        for (entry in entryList) {
            pubs.add(entryToPub(entry))
        }
        return pubs
    }

}