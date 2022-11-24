package com.example.cv2.mapper

import com.example.cv2.data.entity.Pub
import com.example.cv2.data.jsonmapper.Entry
import com.example.cv2.data.response.PubResponseBody

class PubMapper {

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
            phone = entry.tags.phone,
            users = 5678,
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

    fun pubResponseToPubEntity(
        pub: PubResponseBody
    ) : Pub {
        return Pub(
            id = 0,
            importedId = pub.barId.toLong(),
            lat = pub.lat?.toDouble(),
            lon = pub.lon?.toDouble(),
            phone = "TODO",
            name = pub.barName,
            openingHours = "TODO",
            website = "TODO",
            users = pub.users)
    }

    fun pubResponseListToPubEntityList(
        pubList: List<PubResponseBody>
    ) : List<Pub> {
        val pubs = mutableListOf<Pub>()
        for (entry in pubList) {
            pubs.add(pubResponseToPubEntity(entry))
        }
        return pubs
    }

}